package com.api.mocuti.controller

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Participacao
import com.api.mocuti.repository.ParticipacaoRepository
import com.api.mocuti.service.ParticipacaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@Tag(name = "Participação", description = "Operações relacionadas às participações")
@RequestMapping("/participacoes")
class ParticipacaoJpaController(
    var repositorioParticipacao: ParticipacaoRepository,
    val participacaoService: ParticipacaoService
) {

    @Operation(
        summary = "Listar todas as participações",
        description = "Retorna todas as participações cadastradas no sistema"
    )
    @GetMapping
    fun getParticipacao(): ResponseEntity<List<Participacao>> {
        val participacoes = repositorioParticipacao.findAll()

        return if (participacoes.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(participacoes)
        }
    }

    @GetMapping("/participacao-comentar/{idUsuario}")
    fun listarParticipacoes(@PathVariable idUsuario: Int): List<ParticipacaoFeedbackDTO> {
        val cincoDiasAtras = LocalDate.now().minusDays(5)
        return participacaoService.listarParticipacoesFiltradasPorUsuario(idUsuario, cincoDiasAtras)
    }

    @GetMapping("/participacao-passados/{idUsuario}")
    fun listarEventosPassados(@PathVariable idUsuario: Int): List<ParticipacaoFeedbackDTO> {
        val cincoDiasAtras = LocalDate.now().minusDays(5)
        return participacaoService.listarParticipacoesPassadas(idUsuario, cincoDiasAtras)
    }

    @Operation(summary = "Inscrever usuário em um evento")
    @PostMapping("/{idEvento}/inscrever")
    fun inscreverUsuario(
        @PathVariable idEvento: Int,
        @RequestParam idUsuario: Int,
        @RequestParam idStatusInscricao: Int
    ): ResponseEntity<Void> {
        participacaoService.inscreverUsuario(idEvento, idUsuario, idStatusInscricao)
        return ResponseEntity.status(201).build()
    }

    @Operation(summary = "Cancelar inscrição de um usuário em um evento")
    @DeleteMapping("/{idEvento}/cancelar-inscricao")
    fun cancelarInscricao(
        @PathVariable idEvento: Int,
        @RequestParam idUsuario: Int
    ): ResponseEntity<Void> {
        participacaoService.cancelarInscricao(idEvento, idUsuario)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Listar eventos em que o usuário está inscrito")
    @GetMapping("/eventos-inscritos/{idUsuario}")
    fun listarEventosInscritos(@PathVariable idUsuario: Int): ResponseEntity<List<Evento>> {
        val eventos = participacaoService.listarEventosInscritos(idUsuario)
        return if (eventos.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(eventos)
    }

}
