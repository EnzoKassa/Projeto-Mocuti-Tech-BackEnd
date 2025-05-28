package com.api.mocuti.controller

import com.api.mocuti.entity.Feedback
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.FeedbackRepository
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/feedback")
class FeedbackJpaController(val repositorio: FeedbackRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Feedback>> {
        val feedback = repositorio.findAll()
        return if (feedback.isEmpty()) {
            ResponseEntity.status(204).build() // No Content
        } else {
            ResponseEntity.status(200).body(feedback) // OK
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Feedback> {
        val feedback = repositorio.findById(id)
        return if (feedback.isPresent) {
            ResponseEntity.status(200).body(feedback.get()) // OK
        } else {
            ResponseEntity.status(404).build() // Not Found
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            ResponseEntity.status(204).build() // No Content
        } else {
            ResponseEntity.status(404).build() // Not Found
        }
    }

    @PostMapping
    fun post(@RequestBody @Valid novoFeedback: Feedback): ResponseEntity<Feedback> {
        return if (novoFeedback.comentario?.isBlank() != false) {
            ResponseEntity.status(400).build() // Bad Request
        } else {
            val feedback = repositorio.save(novoFeedback)
            ResponseEntity.status(201).body(feedback) // Created
        }
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid feedbackAtualizado: Feedback): ResponseEntity<Feedback> {
        return if (!repositorio.existsById(id)) {
            ResponseEntity.status(404).build() // Not Found
        } else {
            val feedbackComId = feedbackAtualizado.copy(id = id)
            val feedback = repositorio.save(feedbackComId)
            ResponseEntity.status(200).body(feedback) // OK
        }
    }
}