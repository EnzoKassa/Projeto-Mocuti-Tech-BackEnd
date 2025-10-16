package com.api.mocuti.controller

import com.api.mocuti.entity.Convite
import com.api.mocuti.entity.Usuario
import com.api.mocuti.service.ConviteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/convites")
class ConviteJpaController(
    private val conviteService: ConviteService
) {

    @PostMapping("/{idEvento}/enviar")
    fun enviarConvite(
        @PathVariable idEvento: Int,
        @RequestParam idRemetente: Int,
        @RequestBody idsConvidados: List<Int>
    ): ResponseEntity<String> {
        conviteService.enviarConvite(idEvento, idRemetente, idsConvidados)
        return ResponseEntity.ok("Convites enviados com sucesso!")
    }

    @PatchMapping("/{idConvite}/aceitar")
    fun aceitarConvite(@PathVariable idConvite: Int): ResponseEntity<Void> {
        conviteService.aceitarConvite(idConvite)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{idConvite}/cancelar")
    fun cancelarConvite(@PathVariable idConvite: Int): ResponseEntity<Void> {
        conviteService.cancelarConvite(idConvite)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/usuario/{idUsuario}")
    fun listarConvitesPorUsuario(@PathVariable idUsuario: Int): ResponseEntity<List<Convite>> {
        val convites = conviteService.listarConvitesPorUsuario(idUsuario)
        return if (convites.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(convites)
    }

    @GetMapping("/evento/{idEvento}")
    fun listarConvitesPorEvento(@PathVariable idEvento: Int): ResponseEntity<List<Convite>> {
        val convites = conviteService.listarConvitesPorEvento(idEvento)
        return if (convites.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(convites)
    }
}