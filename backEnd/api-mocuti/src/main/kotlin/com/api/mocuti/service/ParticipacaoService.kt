package com.api.mocuti.service

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.repository.FeedbackRepository
import com.api.mocuti.repository.ParticipacaoRepository
import org.springframework.stereotype.Service

@Service
class ParticipacaoService(
    val participacaoRepository: ParticipacaoRepository,
    val feedbackRepository: FeedbackRepository
) {

    fun listarParticipacoesFiltradasPorUsuario(idUsuario: Int): List<ParticipacaoFeedbackDTO> {
        val participacoes = participacaoRepository.listarParticipacoesComFeedback(idUsuario)

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