package com.api.mocuti.controller

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.entity.Participacao
import com.api.mocuti.repository.ParticipacaoRepository
import com.api.mocuti.service.ParticipacaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

    @GetMapping("/filtradas/{idUsuario}")
    fun listarParticipacoes(@PathVariable idUsuario: Int): List<ParticipacaoFeedbackDTO> {
        return participacaoService.listarParticipacoesFiltradasPorUsuario(idUsuario)
    }
}
