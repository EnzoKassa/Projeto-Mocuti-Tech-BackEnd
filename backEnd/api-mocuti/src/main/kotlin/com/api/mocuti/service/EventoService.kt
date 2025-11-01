package com.api.mocuti.service

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Evento
import com.api.mocuti.repository.*
import com.api.mocuti.specification.EventoSpecification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Service
class EventoService(
    private val eventoRepository: EventoRepository,
    private val enderecoRepository: EnderecoRepository,
    private val statusEventoRepository: StatusEventoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val preferenciaRepository: PreferenciaRepository,
    private val emailService: EmailService
) {

    fun listarTodos(): List<Evento> =
        eventoRepository.findAll()

    fun buscarPorId(id: Int): Evento? =
        eventoRepository.findByIdOrNull(id)

    fun deletar(id: Int): Boolean {
        return if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id)
            true
        } else false
    }

    fun criarEvento(dto: EventoCadastroRequest, foto: MultipartFile?): Evento {
        val endereco = enderecoRepository.findById(dto.enderecoId)
            .orElseThrow { IllegalArgumentException("Endereço não encontrado") }

        val status = statusEventoRepository.findById(dto.statusEventoId)
            .orElseThrow { IllegalArgumentException("Status não encontrado") }

        val categoria = categoriaRepository.findById(dto.categoriaId)
            .orElseThrow { IllegalArgumentException("Categoria não encontrada") }

        val evento = Evento(
            idEvento = 0,
            nomeEvento = dto.nomeEvento,
            descricao = dto.descricao,
            dia = dto.dia,
            horaInicio = dto.horaInicio,
            horaFim = dto.horaFim,
            isAberto = dto.isAberto,
            qtdVaga = dto.qtdVaga,
            publicoAlvo = dto.publicoAlvo,
            qtdInteressado = dto.qtdInteressado,
            foto = foto?.bytes ?: ByteArray(0), // se não vier imagem, salva vazio
            endereco = endereco,
            statusEvento = status,
            categoria = categoria
        )

        // Envia email para usuários interessados
        val usuarios = preferenciaRepository.findUsuariosByIdCategoria(evento.categoria.idCategoria!!)
        usuarios.forEach { usuario ->
            emailService.enviarEmailNovoEvento(usuario.email, usuario.nomeCompleto, evento)
        }

        return eventoRepository.save(evento)
    }


    fun atualizarEvento(id: Int, dto: EventoAtualizarRequest): Evento {
        val eventoExistente = eventoRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Evento não encontrado") }

        val endereco = enderecoRepository.findById(dto.enderecoId)
            .orElseThrow { IllegalArgumentException("Endereço não encontrado") }

        val status = statusEventoRepository.findById(dto.statusEventoId)
            .orElseThrow { IllegalArgumentException("Status não encontrado") }

        val categoria = categoriaRepository.findById(dto.categoriaId)
            .orElseThrow { IllegalArgumentException("Categoria não encontrada") }

        eventoExistente.apply {
            nomeEvento = dto.nomeEvento
            descricao = dto.descricao
            dia = dto.dia
            horaInicio = dto.horaInicio
            horaFim = dto.horaFim
            isAberto = dto.isAberto
            qtdVaga = dto.qtdVaga
            publicoAlvo = dto.publicoAlvo
            qtdInteressado = dto.qtdInteressado
            this.endereco = endereco
            this.statusEvento = status
            this.categoria = categoria
        }

        return eventoRepository.save(eventoExistente)
    }

    fun atualizarDiaHora(id: Int, dto: EventoAttDiaHoraRequest): Evento {
        if (!eventoRepository.existsById(id)) {
            throw NoSuchElementException("Evento com ID $id não encontrado")
        }
        eventoRepository.atualizarDiaHora(id, dto.dia, dto.horaInicio, dto.horaFim)
        return eventoRepository.findById(id).get()
    }

    fun atualizarStatusEvento(idEvento: Int, dto: EventoAtualizaStatusRequest): Evento {
        val evento = eventoRepository.findById(idEvento)
            .orElseThrow { NoSuchElementException("Evento com ID $idEvento não encontrado") }

        val novoStatus = statusEventoRepository.findById(dto.idStatusEvento)
            .orElseThrow { NoSuchElementException("StatusEvento com ID ${dto.idStatusEvento} não encontrado") }

        evento.statusEvento = novoStatus
        return eventoRepository.save(evento)
    }


    fun atualizarFoto(id: Int, fotoRequest: ByteArray): FotoRequest {
        val evento = eventoRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Evento com ID $id não encontrado")

        // garante que a data não quebre a validação
        evento.dia = evento.dia.takeIf { !it.isBefore(LocalDate.now()) } ?: LocalDate.now()

        evento.foto = fotoRequest
        val eventoSalvo = eventoRepository.save(evento)

        // retorna só o DTO, não a entity inteira
        return FotoRequest(
            foto = eventoSalvo.foto!!
        )
    }

    fun getFoto(id: Int): ByteArray? =
        eventoRepository.findByIdOrNull(id)?.foto

    fun getEventosUsuario(): List<EventosUsuariosRequest> =
        eventoRepository.getEventosUsuario()

    fun getEventosUsuarioId(idUsuario: Int): EventosUsuariosRequest =
        eventoRepository.getEventosUsuarioPorId(idUsuario)

    fun buscarComFiltros(filtro: EventoFiltroRequest): List<EventoDTO> =
        eventoRepository.findAll(EventoSpecification.comFiltros(filtro))
            .map {
                // Usando argumentos nomeados para mapear corretamente todos os campos da Evento Entity
                EventoDTO(
                    idEvento = it.idEvento,
                    nomeEvento = it.nomeEvento,
                    nome = it.nomeEvento, // Preenchemos 'nome' e 'nomeEvento' para evitar o erro do frontend
                    descricao = it.descricao,
                    dia = it.dia,
                    horaInicio = it.horaInicio,
                    horaFim = it.horaFim,
                    qtdVaga = it.qtdVaga,
                    qtdInteressado = it.qtdInteressado,
                    publicoAlvo = it.publicoAlvo,
                    statusEvento = it.statusEvento,
                    categoria = it.categoria
                )
            }

    fun buscarPorCategoria(categoriaId: Int): List<EventoDTO> =
        eventoRepository.findByCategoria_IdCategoria(categoriaId)
            .map {
                // Repete o mapeamento completo
                EventoDTO(
                    idEvento = it.idEvento,
                    nomeEvento = it.nomeEvento,
                    nome = it.nomeEvento,
                    descricao = it.descricao,
                    dia = it.dia,
                    horaInicio = it.horaInicio,
                    horaFim = it.horaFim,
                    qtdVaga = it.qtdVaga,
                    qtdInteressado = it.qtdInteressado,
                    publicoAlvo = it.publicoAlvo,
                    statusEvento = it.statusEvento,
                    categoria = it.categoria
                )
            }

    fun buscarPorStatus(statusEventoId: Int): List<EventoDTO> =
        eventoRepository.findByStatusEvento_IdStatusEvento(statusEventoId)
            .map {
                EventoDTO(
                    idEvento = it.idEvento,
                    nomeEvento = it.nomeEvento,
                    nome = it.nomeEvento,
                    descricao = it.descricao,
                    dia = it.dia,
                    horaInicio = it.horaInicio,
                    horaFim = it.horaFim,
                    qtdVaga = it.qtdVaga,
                    qtdInteressado = it.qtdInteressado,
                    publicoAlvo = it.publicoAlvo,
                    statusEvento = it.statusEvento,
                    categoria = it.categoria
                )
            }

    fun listarPublicoAlvo(): List<String> {
        return eventoRepository.findAll().mapNotNull { it.publicoAlvo }.distinct()
    }
}
