package com.api.mocuti.service

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Feedback
import com.api.mocuti.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val notaRepository: NotaFeedbackRepository,
    private val eventoRepository: EventoRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun listarTodos(): List<Feedback> = feedbackRepository.findAll()

    fun buscarPorId(id: Int): Feedback? =
        feedbackRepository.findById(id).orElse(null)

    fun deletar(id: Int): Boolean {
        return if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id)
            true
        } else false
    }

    @Transactional
    fun criarOuAtualizar(request: FeedbackNovoRequest): Feedback {
        val evento = eventoRepository.findById(request.idEvento)
            .orElseThrow { IllegalArgumentException("Evento não encontrado") }

        val usuario = usuarioRepository.findById(request.idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        val feedbackExistente = feedbackRepository.findByUsuarioAndEvento(usuario, evento)

        val nota = request.idNota?.let {
            notaRepository.findById(it)
                .orElseThrow { IllegalArgumentException("Nota inválida") }
        }

        return if (feedbackExistente != null) {
            feedbackExistente.comentario = request.comentario
            feedbackExistente.nota = nota
            feedbackRepository.save(feedbackExistente)
        } else {
            feedbackRepository.save(
                Feedback(
                    comentario = request.comentario,
                    evento = evento,
                    usuario = usuario,
                    nota = nota,
                    idFeedback = null
                )
            )
        }
    }

    fun getFeedbackPorCategoria(): List<FeedbacksPorCategoriaRequest> =
        feedbackRepository.getFeedbacksPorCategoria()

    fun getFeedbackCategoriaMesAtual(): List<FeedbackCategoriaMesAtualRequest> =
        feedbackRepository.getFeedbackCategoriaMesAtual()

    fun getFeedbackEvento(): List<FeedbackEventoRequest> =
        feedbackRepository.getFeedbackEvento()
}
