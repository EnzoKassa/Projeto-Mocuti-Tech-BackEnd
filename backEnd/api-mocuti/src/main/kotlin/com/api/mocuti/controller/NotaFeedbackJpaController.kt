package com.api.mocuti.controller

import com.api.mocuti.entity.NotaFeedback
import com.api.mocuti.repository.NotaFeedbackRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notas-feedbacks")
class NotaFeedbackJpaController(var repositorioNotaFeedback: NotaFeedbackRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<NotaFeedback>> {
        val notaFeedback = repositorioNotaFeedback.findAll()

        return if (notaFeedback.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(notaFeedback)
        }
    }
}