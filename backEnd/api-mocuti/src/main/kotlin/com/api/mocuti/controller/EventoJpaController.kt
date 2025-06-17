package com.api.mocuti.controller

import com.api.mocuti.entity.Evento
import com.api.mocuti.repository.EventoRepository
import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Time
import java.time.LocalDate

@RestController
@RequestMapping("/eventos")
class EventoJpaController(var repositorioEvento: EventoRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Evento>> {
        val eventos = repositorioEvento.findAll()

        if (eventos.isEmpty()) {
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(200).body(eventos)
    }

    @GetMapping("/{id}")
    fun getPorId(@PathVariable id: Int): ResponseEntity<Evento> {
        if (!repositorioEvento.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        return ResponseEntity.status(200).body(repositorioEvento.findById(id).get())
    }

    @PostMapping
    fun post(@RequestBody @Valid evento: Evento): ResponseEntity<Evento> {
        val eventoSalvo = repositorioEvento.save(evento)
        return ResponseEntity.status(201).body(eventoSalvo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        if (repositorioEvento.existsById(id)) {
            repositorioEvento.deleteById(id)
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(404).build()
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody eventoAtualizado: Evento): ResponseEntity<Evento> {
        if (!repositorioEvento.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        eventoAtualizado.idEvento = id
        val evento = repositorioEvento.save(eventoAtualizado)

        return ResponseEntity.status(200).body(evento)
    }

    @PatchMapping("/{id}")
    fun patchDiaHora(
        @PathVariable id: Int,
        @RequestParam dia: LocalDate,
        @RequestParam horaInicio: Time,
        @RequestParam horaFim: Time
    ): ResponseEntity<Evento> {
        if (!repositorioEvento.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        repositorioEvento.atualizarDiaHora(id, dia, horaInicio, horaFim)
        val eventoEncontrado = repositorioEvento.findById(id).get()

        return ResponseEntity.status(200).body(eventoEncontrado)
    }

    @GetMapping(
        value = ["/foto/{id}"],
        produces = ["image/png", "image/jpeg", "image/jpg"]
    )
    fun getFoto(@PathVariable id: Int): ResponseEntity<ByteArray> {
        if (!repositorioEvento.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        val fotoEvento = repositorioEvento.findByIdOrNull(id)
        val foto = fotoEvento!!.foto

        return ResponseEntity.status(200).body(foto)
    }

    // ENDPOINT de foto
    @PatchMapping("/foto/{id}")
    fun patchFoto(@PathVariable id: Int, @RequestBody foto: ByteArray): ResponseEntity<Evento> {
        if (!repositorioEvento.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        val fotoEvento = repositorioEvento.findByIdOrNull(id)
        fotoEvento!!.foto = foto
        repositorioEvento.save(fotoEvento)

        return ResponseEntity.status(200).body(fotoEvento)
    }
}