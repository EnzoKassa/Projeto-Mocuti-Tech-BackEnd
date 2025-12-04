package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Feedback
import com.api.mocuti.service.FeedbackService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Feedback", description = "Operações relacionadas aos feedbacks")
@RequestMapping("/feedback")
class FeedbackJpaController(
    private val feedbackService: FeedbackService
) {

    @Operation(summary = "Listar todos os feedbacks",
        description = "Retorna uma lista de todos os feedbacks cadastrados no sistema")
    @GetMapping
    fun getFeedback(): ResponseEntity<List<Feedback>> {
        val feedbacks = feedbackService.listarTodos()
        return if (feedbacks.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(feedbacks)
    }

    @Operation(summary = "Buscar feedback por ID",
        description = "Retorna o feedback correspondente ao ID fornecido")
    @GetMapping("/{id}")
    fun getFeedbackPorId(@PathVariable id: Int): ResponseEntity<Feedback> {
        val feedback = feedbackService.buscarPorId(id)
        return if (feedback != null) ResponseEntity.ok(feedback)
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Deletar um feedback",
        description = "Exclui o feedback correspondente ao ID fornecido")
    @DeleteMapping("/{id}")
    fun deleteFeedback(@PathVariable id: Int): ResponseEntity<Void> {
        return if (feedbackService.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Criar ou atualizar feedback",
        description = "Cria um novo feedback ou atualiza um existente com base nos dados fornecidos")
    @PostMapping
    fun postFeedback(@RequestBody novoFeedback: FeedbackNovoRequest): ResponseEntity<Feedback> {
        val feedback = feedbackService.criarOuAtualizar(novoFeedback)
        return ResponseEntity.ok(feedback)
    }

    @Operation(summary = "Feedbacks por categoria",
        description = "Retorna a quantidade de feedbacks agrupados por categoria")
    @GetMapping("/view/feedbacks-por-categoria")
    fun getFeedbackCategoria(): ResponseEntity<List<FeedbacksPorCategoriaRequest>> {
        return ResponseEntity.ok(feedbackService.getFeedbackPorCategoria())
    }

    @Operation(summary = "Feedbacks por categoria no mês atual",
        description = "Retorna a quantidade de feedbacks agrupados por categoria no mês atual")
    @GetMapping("/view/feedback-categoria-mes-atual")
    fun getFeedbackCategoriaMesAtual(): ResponseEntity<List<FeedbackCategoriaMesAtualRequest>> {
        return ResponseEntity.ok(feedbackService.getFeedbackCategoriaMesAtual())
    }

    @Operation(summary = "Feedbacks por evento",
        description = "Retorna a quantidade de feedbacks agrupados por evento")
    @GetMapping("/view/feedback-evento")
    fun getFeedbackEvento(): ResponseEntity<List<FeedbackEventoRequest>> {
        return ResponseEntity.ok(feedbackService.getFeedbackEvento())
    }
}
