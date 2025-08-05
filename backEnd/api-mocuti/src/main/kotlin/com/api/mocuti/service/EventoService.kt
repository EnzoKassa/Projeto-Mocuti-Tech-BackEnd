import com.api.mocuti.dto.EventoAttDiaHoraRequest
import com.api.mocuti.dto.EventoAtualizaStatusRequest
import com.api.mocuti.dto.EventoAtualizarRequest
import com.api.mocuti.dto.EventoCadastroRequest
import com.api.mocuti.entity.Evento
import com.api.mocuti.repository.*
import org.springframework.stereotype.Service

@Service
class EventoService(
    val eventoRepository: EventoRepository,
    val enderecoRepository: EnderecoRepository,
    val statusEventoRepository: StatusEventoRepository,
    val categoriaRepository: CategoriaRepository
) {
    fun criarEvento(dto: EventoCadastroRequest): Evento {
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
            foto = ByteArray(0),
            endereco = endereco,
            statusEvento = status,
            categoria = categoria
        )

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

        // Atualiza campos do evento existente
        eventoExistente.nomeEvento = dto.nomeEvento
        eventoExistente.descricao = dto.descricao
        eventoExistente.dia = dto.dia
        eventoExistente.horaInicio = dto.horaInicio
        eventoExistente.horaFim = dto.horaFim
        eventoExistente.isAberto = dto.isAberto
        eventoExistente.qtdVaga = dto.qtdVaga
        eventoExistente.publicoAlvo = dto.publicoAlvo
        eventoExistente.qtdInteressado = dto.qtdInteressado
        eventoExistente.endereco = endereco
        eventoExistente.statusEvento = status
        eventoExistente.categoria = categoria

        return eventoRepository.save(eventoExistente)
    }

    fun atualizarDiaHora(id: Int, dto: EventoAttDiaHoraRequest): Evento {
        val evento = eventoRepository.findById(id)
            .orElseThrow { NoSuchElementException("Evento com ID $id não encontrado") }

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

}