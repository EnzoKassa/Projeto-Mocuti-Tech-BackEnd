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
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(feedback)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Feedback> {
        val feedback = repositorio.findById(id)
        return ResponseEntity.of(feedback)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun post(@RequestBody @Valid novoFeedback: Feedback): ResponseEntity<Feedback> {
        val feedback = repositorio.save(novoFeedback)
        return ResponseEntity.status(201).body(feedback)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid feedbackAtualizado: Feedback): ResponseEntity<Feedback> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        val feedbackComId = feedbackAtualizado.copy(id = id)
        val feedback = repositorio.save(feedbackComId)
        return ResponseEntity.ok(feedback)
    }
}