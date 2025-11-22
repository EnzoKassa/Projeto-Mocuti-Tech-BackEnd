package com.api.mocuti.service

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Participacao
import com.api.mocuti.entity.ParticipacaoId
import com.api.mocuti.repository.ParticipacaoRepository
import com.api.mocuti.repository.FeedbackRepository
import com.api.mocuti.repository.EventoRepository
import com.api.mocuti.repository.UsuarioRepository
import com.api.mocuti.repository.StatusInscricaoRepository
import jakarta.transaction.Transactional
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
    val emailService: EmailService
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
                email = array[3] as String?,       // NOVO CAMPO (Índice 3)
                telefone = array[4] as String?,    // NOVO CAMPO (Índice 4)
                tipoCargo = array[5] as String,    // Índice ajustado de 3 para 5
                nomeEvento = array[6] as String,   // Índice ajustado de 4 para 6
                isInscrito = (array[7] as Number).toInt() == 1, // Índice ajustado de 5 para 7
                isPresente = (array[8] as Number?)?.toInt() == 1, // Índice ajustado de 6 para 8
                tipoInscricao = array[9] as String // Índice ajustado de 7 para 9
            )
        }
    }

    fun registrarPresenca(idEvento: Int, listaPresenca: List<PresencaDTO>): Int {
        // Separa os usuários que terão a presença MARCADA (true)
        val usuariosPresentes = listaPresenca
            .filter { it.presente }
            .map { it.idUsuario }
            .distinct()

        // Separa os usuários que terão a presença REMOVIDA (false)
        val usuariosAusentes = listaPresenca
            .filter { !it.presente }
            .map { it.idUsuario }
            .distinct()

        var totalAtualizados = 0

        // 1. Atualiza quem está presente
        if (usuariosPresentes.isNotEmpty()) {
            totalAtualizados += participacaoRepository.bulkUpdatePresenca(
                idEvento = idEvento,
                idsUsuarios = usuariosPresentes,
                presenca = true
            )
        }

        // 2. Atualiza quem está ausente
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

    fun listarEventosConfirmados(usuarioId: Int): List<ParticipacaoResponse> {
        return participacaoRepository.findEventosPresentesPorUsuario(usuarioId)
    }

    @Transactional
    fun atualizarStatusParticipacao(request: AtualizarPresencaRequest): Boolean {

        val linhasAfetadas = participacaoRepository.atualizarStatusParticipacao(
            usuarioId = request.usuarioId,
            eventoId = request.eventoId,
            statusInscricaoId = request.statusInscricaoId
        )

        // Se não atualizou nenhuma linha, nada a fazer
        if (linhasAfetadas <= 0) {
            return false
        }

        // Buscar participação atualizada com JOIN (pra ter usuario, evento e status)
        val participacaoAtualizada = participacaoRepository.findByUsuario_IdUsuarioAndEvento_IdEvento(
            request.usuarioId,
            request.eventoId
        )

        // Agora sim conseguimos enviar o e-mail
        participacaoAtualizada?.let { participacao ->
            emailService.enviarEmailStatusParticipacao(
                participacao.usuario.email,
                participacao.usuario.nomeCompleto,
                participacao.statusInscricao.tipoInscricao,
                participacao.evento
            )
        }

        return true
    }

}
