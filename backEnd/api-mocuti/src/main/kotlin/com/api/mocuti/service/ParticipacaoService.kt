package com.api.mocuti.service

import com.api.mocuti.dto.ConvidadoEventoDTO
import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.dto.PresencaDTO
import com.api.mocuti.dto.UsuariosInscritosCargo2DTO
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Participacao
import com.api.mocuti.entity.ParticipacaoId
import com.api.mocuti.repository.ParticipacaoRepository
import com.api.mocuti.repository.FeedbackRepository
import com.api.mocuti.repository.EventoRepository
import com.api.mocuti.repository.UsuarioRepository
import com.api.mocuti.repository.StatusInscricaoRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

// Importações adicionais para o EmailService
import org.springframework.mail.javamail.JavaMailSender // Pode remover se EmailService já estiver injetado
import org.springframework.mail.javamail.MimeMessageHelper // Pode remover se EmailService já estiver injetado

@Service
class ParticipacaoService(
    val participacaoRepository: ParticipacaoRepository,
    val feedbackRepository: FeedbackRepository,
    val eventoRepository: EventoRepository,
    val usuarioRepository: UsuarioRepository,
    val statusInscricaoRepository: StatusInscricaoRepository,
    private val emailService: EmailService // Injete o EmailService aqui
) {

    fun listarParticipacoesFiltradasPorUsuario(idUsuario: Int, dia: LocalDate): List<ParticipacaoFeedbackDTO> {
        val participacoes = participacaoRepository.listarParticipacoesComFeedback(idUsuario, dia)

        return participacoes.map { p ->
            val feedback = feedbackRepository.findByUsuarioAndEvento(p.usuario, p.evento)
            ParticipacaoFeedbackDTO(
                id = p.id,
                isPresente = p.isPresente,
                email = p.usuario.email,
                nomeEvento = p.evento.nomeEvento,
                nota = feedback?.nota,
                comentario = feedback?.comentario
            )
        }
    }

    fun listarParticipacoesPassadas(idUsuario: Int, diaLimite: LocalDate): List<ParticipacaoFeedbackDTO> {
        val participacoes = participacaoRepository.listarParticipacoesPassadas(idUsuario, diaLimite)

        return participacoes.map { p ->
            val feedback = feedbackRepository.findByUsuarioAndEvento(p.usuario, p.evento)
            ParticipacaoFeedbackDTO(
                id = p.id,
                isPresente = p.isPresente,
                email = p.usuario.email,
                nomeEvento = p.evento.nomeEvento,
                nota = feedback?.nota,
                comentario = feedback?.comentario
            )
        }
    }

    fun cancelarInscricao(idEvento: Int, idUsuario: Int) {
        val participacao = participacaoRepository.findById(ParticipacaoId(usuarioId = idUsuario, eventoId = idEvento))
            .orElseThrow { NoSuchElementException("Participação não encontrada para o evento $idEvento e usuário $idUsuario") }

        val idStatus = participacao.evento.statusEvento.idStatusEvento
        if (idStatus == 2 || idStatus == 3) {
            throw IllegalStateException("Não é possível cancelar a inscrição de um evento que já está acontecendo ou encerrado.")
        }

        participacaoRepository.delete(participacao)
    }

    fun listarEventosInscritos(idUsuario: Int): List<Evento> {
        val participacoes =
            participacaoRepository.findByUsuario_IdUsuarioAndIsInscritoTrueAndEventoStatusAberto(idUsuario)
        return participacoes.map { it.evento }
    }

    fun listarUsuariosInscritosRestrito(idEvento: Int): List<UsuariosInscritosCargo2DTO> {
        val resultados = participacaoRepository.listarUsuariosInscritosCargo2PorEvento(idEvento)

        return resultados.map { array ->
            UsuariosInscritosCargo2DTO(
                idEvento = (array[0] as Number).toInt(),
                idUsuario = (array[1] as Number).toInt(),
                nomeCompleto = array[2] as String,
                email = array[3] as String?,
                telefone = array[4] as String?,
                tipoCargo = array[5] as String,
                nomeEvento = array[6] as String,
                isInscrito = (array[7] as Number).toInt() == 1,
                isPresente = (array[8] as Number?)?.toInt() == 1,
                tipoInscricao = array[9] as String
            )
        }
    }

    fun registrarPresenca(idEvento: Int, listaPresenca: List<PresencaDTO>): Int {
        val usuariosPresentes = listaPresenca
            .filter { it.presente }
            .map { it.idUsuario }
            .distinct()

        val usuariosAusentes = listaPresenca
            .filter { !it.presente }
            .map { it.idUsuario }
            .distinct()

        var totalAtualizados = 0

        if (usuariosPresentes.isNotEmpty()) {
            totalAtualizados += participacaoRepository.bulkUpdatePresenca(
                idEvento = idEvento,
                idsUsuarios = usuariosPresentes,
                presenca = true
            )
        }

        if (usuariosAusentes.isNotEmpty()) {
            totalAtualizados += participacaoRepository.bulkUpdatePresenca(
                idEvento = idEvento,
                idsUsuarios = usuariosAusentes,
                presenca = false
            )
        }

        return totalAtualizados
    }

    fun contarUsuariosInscritosCargo2(idEvento: Int): Long {
        return participacaoRepository.countUsuariosInscritosCargo2PorEvento(idEvento)
    }

    fun listarConvidadosPorEvento(idEvento: Int): List<ConvidadoEventoDTO> {
        val resultados = participacaoRepository.listarConvidadosPorEvento(idEvento)
        return resultados.map { array ->
            ConvidadoEventoDTO(
                idEvento = (array[0] as Number).toInt(),
                idUsuario = (array[1] as Number).toInt(),
                nomeConvidado = array[2] as String,
                statusConvite = array[3] as String
            )
        }
    }

    fun inscreverUsuario(idEvento: Int, idUsuario: Int, idStatusInscricao: Int) {
        val participacaoExistente =
            participacaoRepository.findById(ParticipacaoId(usuarioId = idUsuario, eventoId = idEvento))
        if (participacaoExistente.isPresent) {
            throw IllegalStateException("Usuário já está inscrito neste evento.")
        }

        val evento = eventoRepository.findById(idEvento)
            .orElseThrow { NoSuchElementException("Evento com ID $idEvento não encontrado") }

        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { NoSuchElementException("Usuário com ID $idUsuario não encontrado") }

        val statusInscricao = statusInscricaoRepository.findById(idStatusInscricao)
            .orElseThrow { NoSuchElementException("StatusInscricao com ID $idStatusInscricao não encontrado") }

        val participacao = Participacao(
            id = ParticipacaoId(usuarioId = idUsuario, eventoId = idEvento),
            isInscrito = true,
            isPresente = false,
            statusInscricao = statusInscricao,
            usuario = usuario,
            evento = evento
        )

        participacaoRepository.save(participacao)

        // Declaração das variáveis fora do bloco if
        val destinatarioEmail: String?
        val nomeUsuario: String?

        if (usuario.cargo?.idCargo == 3) {
            destinatarioEmail = usuario.email
            nomeUsuario = usuario.nomeCompleto
        } else {
            throw IllegalStateException("Usuário não possui o cargo necessário para receber o e-mail.")
        }

        // Chama o método 'enviarEmailConviteEvento' do EmailService
        emailService.enviarEmailConviteEvento(
            destinatarioEmail,
            nomeUsuario ?: throw IllegalStateException("Nome do usuário não pode ser nulo."),
            evento
        )
    }
}