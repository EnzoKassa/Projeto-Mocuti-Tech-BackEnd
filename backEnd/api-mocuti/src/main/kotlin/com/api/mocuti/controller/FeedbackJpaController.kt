package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Feedback
import com.api.mocuti.repository.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.service.FeedbackService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@Tag(name = "Feedback", description = "Operações relacionadas aos feedbacks")
@RequestMapping("/feedback")
class FeedbackJpaController(
    val repositorio: FeedbackRepository,
    val eventoRepository: EventoRepository,
    val usuarioRepository: UsuarioRepository,
    val notaFeedbackRepository: NotaFeedbackRepository,
    val participacaoRepository: ParticipacaoRepository,
    val feedbackService: FeedbackService = FeedbackService(
        repositorio,
        notaFeedbackRepository,
        eventoRepository,
        usuarioRepository
    )
) {
    @Operation(
        summary = "Listar todos os feedbacks",
        description = "Retorna todos os feedbacks cadastrados"
    )
    @GetMapping
    fun getFeedback(): ResponseEntity<List<Feedback>> {
        val feedback = repositorio.findAll()
        return if (feedback.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(feedback)
        }
    }

    @Operation(
        summary = "Buscar feedback por ID",
        description = "Retorna o feedback correspondente ao ID fornecido"
    )
    @GetMapping("/{id}")
    fun getFeedbackPorId(@PathVariable id: Int): ResponseEntity<Feedback> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        val feedback = repositorio.findById(id).get()
        return ResponseEntity.status(200).body(feedback)
    }

    @Operation(
        summary = "Deletar um feedback",
        description = "Remove o feedback com o ID fornecido"
    )
    @DeleteMapping("/{id}")
    fun deleteFeedback(@PathVariable id: Int): ResponseEntity<Void> {
        return if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @Operation(
        summary = "Criar ou atualizar feedback",
        description = "Se já existe feedback do usuário no evento, atualiza; caso contrário, cria"
    )
    @PostMapping
    fun postFeedback(@RequestBody novoFeedback: FeedbackNovoRequest): ResponseEntity<Feedback> {
        val feedback = feedbackService.criarOuAtualizar(novoFeedback)
        return ResponseEntity.ok(feedback)
    }

    @Operation(
        summary = "Feedbacks por categoria",
        description = "Quantidade de feedbacks agrupados por categoria"
    )
    @GetMapping("/view/feedbacks-por-categoria")
    fun getFeedbackCategoria(): ResponseEntity<List<FeedbacksPorCategoriaRequest>> {
        val feedback = feedbackService.getFeedbackPorCategoria()
        return ResponseEntity.status(200).body(feedback)
    }

    @Operation(
        summary = "Feedbacks por categoria no mês atual",
        description = "Quantidade de feedbacks agrupados por categoria do mês atual"
    )
    @GetMapping("/view/feedback-categoria-mes-atual")
    fun getFeedbackCategoriaMesAtual(): ResponseEntity<List<FeedbackCategoriaMesAtualRequest>> {
        val feedback = feedbackService.getFeedbackCategoriaMesAtual()
        return ResponseEntity.status(200).body(feedback)
    }

    @Operation(
        summary = "Feedbacks por evento",
        description = "Quantidade de feedbacks agrupados por evento"
    )
    @GetMapping("/view/feedback-evento")
    fun getFeedbackEvento(): ResponseEntity<List<FeedbackEventoRequest>> {
        val feedback = feedbackService.getFeedbackEvento()
        return ResponseEntity.status(200).body(feedback)
    }
}