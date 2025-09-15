package com.api.mocuti.service

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Feedback
import com.api.mocuti.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FeedbackService(
    val feedbackRepository: FeedbackRepository,
    val notaRepository: NotaFeedbackRepository,
    val eventoRepository: EventoRepository,
    val usuarioRepository: UsuarioRepository
) {

    @Transactional
    fun criarOuAtualizar(request: FeedbackNovoRequest): Feedback {
        val evento = eventoRepository.findById(request.idEvento)
            .orElseThrow { IllegalArgumentException("Evento não encontrado") }

        val usuario = usuarioRepository.findById(request.idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        // Verifica se já existe feedback
        val feedbackExistente = feedbackRepository.findByUsuarioAndEvento(usuario, evento)

        // Se forneceu nota, busca
        val nota = request.idNota?.let {
            notaRepository.findById(it)
                .orElseThrow { IllegalArgumentException("Nota inválida") }
        }

        return if (feedbackExistente != null) {
            // Atualiza
            feedbackExistente.comentario = request.comentario
            feedbackExistente.nota = nota
            feedbackRepository.save(feedbackExistente)
        } else {
            // Cria novo
            var novoFeedback = Feedback(
                comentario = request.comentario,
                evento = evento,
                usuario = usuario,
                nota = nota,
                idFeedback = null
            )
            feedbackRepository.save(novoFeedback)
        }
    }

    fun getFeedbackPorCategoria(): List<FeedbacksPorCategoriaRequest> {
        return feedbackRepository.getFeedbacksPorCategoria()
    }

    fun getFeedbackCategoriaMesAtual(): List<FeedbackCategoriaMesAtualRequest> {
        return feedbackRepository.getFeedbackCategoriaMesAtual()
    }

    fun getFeedbackEvento(): List<FeedbackEventoRequest> {
        return feedbackRepository.getFeedbackEvento()
    }
}