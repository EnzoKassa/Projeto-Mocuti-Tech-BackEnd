package com.api.mocuti.service

import com.api.mocuti.dto.FeedbackAtualizarRequest
import com.api.mocuti.dto.FeedbackNovoRequest
import com.api.mocuti.entity.Feedback
import com.api.mocuti.repository.EventoRepository
import com.api.mocuti.repository.FeedbackRepository
import com.api.mocuti.repository.NotaFeedbackRepository
import com.api.mocuti.repository.UsuarioRepository
import org.springframework.stereotype.Service

@Service
class FeedbackService(
    val feedbackRepository: FeedbackRepository,
    val notaRepository: NotaFeedbackRepository,
    val eventoRepository: EventoRepository,
    val usuarioRepository: UsuarioRepository
) {
    fun criar(request: FeedbackNovoRequest): Feedback {
        val evento = eventoRepository.findById(request.idEvento)
            .orElseThrow { IllegalArgumentException("Evento não encontrado") }

        val usuario = usuarioRepository.findById(request.idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        val feedback = Feedback(
            idFeedback = 0,
            comentario = request.comentario,
            evento = evento,
            usuario = usuario
        )

        return feedbackRepository.save(feedback)
    }

    fun atualizar(id: Int, dto: FeedbackAtualizarRequest): Feedback {
        val feedbackExistente = feedbackRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Feedback não encontrado") }

        if (dto.comentario != null) {
            feedbackExistente.comentario = dto.comentario
        }

        val idNota = dto.idNota
        if (idNota != null) {
            val nota = notaRepository.findById(idNota)
                .orElseThrow { IllegalArgumentException("Nota não encontrada") }
            feedbackExistente.nota = nota
        }

        return feedbackRepository.save(feedbackExistente)
    }
}