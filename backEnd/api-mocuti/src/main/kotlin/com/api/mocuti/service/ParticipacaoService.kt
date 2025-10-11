package com.api.mocuti.service

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
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

@Service
class ParticipacaoService(
    val participacaoRepository: ParticipacaoRepository,
    val feedbackRepository: FeedbackRepository,
    val eventoRepository: EventoRepository,
    val usuarioRepository: UsuarioRepository,
    val statusInscricaoRepository: StatusInscricaoRepository
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
        val participacaoExistente = participacaoRepository.findById(ParticipacaoId(usuarioId = idUsuario, eventoId = idEvento))
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

        participacaoRepository.delete(participacao)
    }

    fun listarEventosInscritos(idUsuario: Int): List<Evento> {
        val participacoes = participacaoRepository.findByUsuario_IdUsuarioAndIsInscritoTrue(idUsuario)
        return participacoes.map { it.evento }
    }
}