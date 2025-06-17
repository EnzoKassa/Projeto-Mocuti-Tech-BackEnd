package com.api.mocuti.controller

import com.api.mocuti.entity.Feedback
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.FeedbackRepository

@RestController
@RequestMapping("/feedback")
class FeedbackJpaController(val repositorio: FeedbackRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Feedback>> {
        val feedback = repositorio.findAll()
        return if (feedback.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(feedback)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Feedback> {
        val feedback = repositorio.findById(id)
        return if (feedback.isPresent) {
            ResponseEntity.status(200).body(feedback.get())
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping
    fun post(@RequestBody @Valid novoFeedback: Feedback): ResponseEntity<Feedback> {
        val feedback = repositorio.save(novoFeedback)
        return ResponseEntity.status(201).body(feedback)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid feedbackAtualizado: Feedback): ResponseEntity<Feedback> {
        return if (!repositorio.existsById(id)) {
            ResponseEntity.status(404).build()
        } else {
            val feedbackComId = feedbackAtualizado.copy(id = id)
            val feedback = repositorio.save(feedbackComId)
            ResponseEntity.status(200).body(feedback)
        }
    }
}