package com.api.mocuti.controller

import EventoService
import com.api.mocuti.dto.*
import com.api.mocuti.entity.Evento
import com.api.mocuti.repository.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Evento", description = "Gerenciamento dos eventos cadastrados no sistema")
@RequestMapping("/eventos")
class EventoJpaController(
    val repositorioEvento: EventoRepository,
    val enderecoRepository: EnderecoRepository,
    val statusEventoRepository: StatusEventoRepository,
    val categoriaRepository: CategoriaRepository,
    val eventoService: EventoService = EventoService(
        repositorioEvento,
        enderecoRepository,
        statusEventoRepository,
        categoriaRepository
    )
) {

    @Operation(
        summary = "Listar todos os eventos",
        description = "Retorna todos os eventos cadastrados no sistema; retorna 204 se não houver eventos"
    )
    @GetMapping
    fun get(): ResponseEntity<List<Evento>> {
        val eventos = repositorioEvento.findAll()

        if (eventos.isEmpty()) {
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(200).body(eventos)
    }

    @Operation(
        summary = "Buscar evento por ID",
        description = "Retorna um evento específico a partir do ID informado"
    )
    @GetMapping("/{id}")
    fun getPorId(@PathVariable id: Int): ResponseEntity<Evento> {
        if (!repositorioEvento.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        return ResponseEntity.status(200).body(repositorioEvento.findById(id).get())
    }

    @Operation(
        summary = "Cadastrar novo evento",
        description = "Cria um novo evento com os dados informados"
    )
    @PostMapping
    fun post(@RequestBody @Valid dto: EventoCadastroRequest): ResponseEntity<Evento> {
        val evento = eventoService.criarEvento(dto)
        return ResponseEntity.status(201).body(evento)
    }

    @Operation(
        summary = "Excluir evento por ID",
        description = "Remove o evento do sistema com base no ID"
    )
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        if (repositorioEvento.existsById(id)) {
            repositorioEvento.deleteById(id)
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(404).build()
    }

    @Operation(
        summary = "Atualizar dados do evento",
        description = "Atualiza todos os campos de um evento existente"
    )
    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody eventoAtualizacaoRequest: EventoAtualizarRequest): ResponseEntity<Evento> {
        val eventoAtualizado = eventoService.atualizarEvento(id, eventoAtualizacaoRequest)
        return ResponseEntity.status(200).body(eventoAtualizado)
    }

    // possivelmente irá para service
    @Operation(
        summary = "Atualizar data e horário do evento",
        description = "Permite alterar somente o dia e os horários de início e fim do evento"
    )
    @PatchMapping("/{id}")
    fun patchDiaHora(
        @PathVariable id: Int,
        @RequestBody eventoAttDiaHoraRequest: EventoAttDiaHoraRequest
    ): ResponseEntity<Evento> {
        val eventoAtualizado = eventoService.atualizarDiaHora(id, eventoAttDiaHoraRequest)

        return ResponseEntity.status(200).body(eventoAtualizado)
    }

    @Operation(
        summary = "Buscar foto do evento",
        description = "Retorna a imagem (foto) vinculada ao evento"
    )
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

    @Operation(
        summary = "Atualizar foto do evento",
        description = "Altera apenas a imagem (foto) do evento"
    )
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

    @Operation(
        summary = "Atualizar status do evento",
        description = "Altera apenas o status (FK) de um evento"
    )
    @PatchMapping("/{id}/status")
    fun patchStatusEvento(
        @PathVariable id: Int,
        @RequestBody dto: EventoAtualizaStatusRequest
    ): ResponseEntity<Evento> {
        val eventoAtualizado = eventoService.atualizarStatusEvento(id, dto)
        return ResponseEntity.ok(eventoAtualizado)
    }

    @Operation(
        summary = "Listar informacoes do evento e usuario responsavel",
        description = "Retorna uma lista com as informacoes do evento e do usuario responsavel por ele"
    )
    @GetMapping("/view/eventos-usuario")
    fun getEventoUsuario(): ResponseEntity<List<EventosUsuariosRequest>> {
        val evento = eventoService.getEventosUsuario()

        return ResponseEntity.status(200).body(evento)
    }

    @Operation(
        summary = "Listar informacoes do evento com id do usuario responsavel",
        description = "Retorna uma lista com as informacoes do evento e do usuario responsavel por ele"
    )
    @GetMapping("/view/eventos-usuario/{idUsuario}")
    fun getEventoUsuarioPorId(@PathVariable idUsuario: Int): ResponseEntity<EventosUsuariosRequest> {
        if (!repositorioEvento.existsById(idUsuario)) {
            return ResponseEntity.status(404).build()
        }

        val evento = eventoService.getEventosUsuarioId(idUsuario)

        return ResponseEntity.status(200).body(evento)
    }
}