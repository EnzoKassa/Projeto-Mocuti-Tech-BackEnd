package com.api.mocuti.service

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.repository.FeedbackRepository
import com.api.mocuti.repository.ParticipacaoRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ParticipacaoService(
    val participacaoRepository: ParticipacaoRepository,
    val feedbackRepository: FeedbackRepository
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
        // Busca participações anteriores a diaLimite
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

}