package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Evento
import com.api.mocuti.service.EventoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@RestController
@Tag(name = "Evento", description = "Gerenciamento dos eventos cadastrados no sistema")
@RequestMapping("/eventos")
class EventoJpaController(
    private val eventoService: EventoService
) {

    @Operation(summary = "Listar todos os eventos")
    @GetMapping
    fun getEvento(): ResponseEntity<List<Evento>> {
        val eventos = eventoService.listarTodos()
        return if (eventos.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(eventos)
    }

    @Operation(summary = "Buscar evento por ID")
    @GetMapping("/{id}")
    fun getPorId(@PathVariable id: Int): ResponseEntity<Evento> {
        val evento = eventoService.buscarPorId(id)
        return if (evento != null) ResponseEntity.ok(evento)
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Cadastrar novo evento")
    @PostMapping("/cadastrar", consumes = ["multipart/form-data"])
    fun postEvento(
        @RequestPart("dados") @Valid dto: EventoCadastroRequest,
        @RequestPart("foto", required = false) foto: MultipartFile?
    ): ResponseEntity<Evento> {
        val evento = eventoService.criarEvento(dto, foto)
        return ResponseEntity.status(201).body(evento)
    }

    @Operation(summary = "Excluir evento por ID")
    @DeleteMapping("/{id}")
    fun deleteEvento(@PathVariable id: Int): ResponseEntity<Void> {
        return if (eventoService.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Atualizar dados do evento")
    @PutMapping("/{id}")
    fun putEvento(@PathVariable id: Int, @RequestBody dto: EventoAtualizarRequest): ResponseEntity<Evento> {
        val eventoAtualizado = eventoService.atualizarEvento(id, dto)
        return ResponseEntity.ok(eventoAtualizado)
    }

    @Operation(summary = "Atualizar data e horário do evento")
    @PatchMapping("/{id}")
    fun patchDiaHora(@PathVariable id: Int, @RequestBody dto: EventoAttDiaHoraRequest): ResponseEntity<Evento> {
        val eventoAtualizado = eventoService.atualizarDiaHora(id, dto)
        return ResponseEntity.ok(eventoAtualizado)
    }

    @Operation(summary = "Buscar foto do evento")
    @GetMapping("/foto/{id}", produces = ["image/png", "image/jpeg", "image/jpg"])
    fun getFoto(@PathVariable id: Int): ResponseEntity<ByteArray> {
        val foto = eventoService.getFoto(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(foto)
    }

    @Operation(summary = "Atualizar foto do evento")
    @PatchMapping("/foto/{id}", consumes = ["multipart/form-data"])
    fun patchFoto(
        @PathVariable id: Int,
        @RequestParam("foto") foto: MultipartFile
    ): ResponseEntity<FotoRequest> {
        val fotoAtualizada = eventoService.atualizarFoto(id, foto.bytes)
        return ResponseEntity.ok(fotoAtualizada)
    }

    @Operation(summary = "Atualizar status do evento")
    @PatchMapping("/{id}/status")
    fun patchStatusEvento(@PathVariable id: Int, @RequestBody dto: EventoAtualizaStatusRequest): ResponseEntity<Evento> {
        val eventoAtualizado = eventoService.atualizarStatusEvento(id, dto)
        return ResponseEntity.ok(eventoAtualizado)
    }

    @Operation(summary = "Listar informacoes do evento e usuario responsavel")
    @GetMapping("/view/eventos-usuario")
    fun getEventoUsuario(): ResponseEntity<List<EventosUsuariosRequest>> {
        val eventos = eventoService.getEventosUsuario()
        return ResponseEntity.ok(eventos)
    }

    @Operation(summary = "Listar informacoes do evento com id do usuario responsavel")
    @GetMapping("/view/eventos-usuario/{idUsuario}")
    fun getEventoUsuarioPorId(@PathVariable idUsuario: Int): ResponseEntity<EventosUsuariosRequest> {
        val evento = eventoService.getEventosUsuarioId(idUsuario)
        return ResponseEntity.ok(evento)
    }

    @GetMapping("/por-eventos")
    fun listarEventos(
        @RequestParam(required = false) nome: String?,
        @RequestParam(required = false) dataInicio: LocalDate?,
        @RequestParam(required = false) dataFim: LocalDate?,
        @RequestParam(required = false) categoriaId: Int?,
        @RequestParam(required = false) statusEventoId: Int?
    ): List<EventoDTO> {
        val filtro = EventoFiltroRequest(nome, dataInicio, dataFim, categoriaId, statusEventoId)
        return eventoService.buscarComFiltros(filtro)
    }

    @GetMapping("/por-categoria")
    fun listarEventosPorCategoria(@RequestParam categoriaId: Int): List<EventoDTO> =
        eventoService.buscarPorCategoria(categoriaId)

    @GetMapping("/status")
    fun listarEventosPorStatus(@RequestParam statusEventoId: Int): List<EventoDTO> =
        eventoService.buscarPorStatus(statusEventoId)

    @Operation(summary = "Listar todos os valores de público alvo dos eventos")
    @GetMapping("/publico-alvo")
    fun listarPublicoAlvo(): ResponseEntity<List<String>> {
        val publicosAlvo = eventoService.listarPublicoAlvo()
        return if (publicosAlvo.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(publicosAlvo)
    }
}