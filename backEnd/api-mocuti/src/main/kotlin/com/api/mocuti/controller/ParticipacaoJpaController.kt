package com.api.mocuti.controller

import com.api.mocuti.dto.AtualizarPresencaRequest
import com.api.mocuti.dto.BulkPresencaRequest
import com.api.mocuti.dto.ConvidadoEventoDTO
import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.dto.UsuariosInscritosCargo2DTO
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Participacao
import com.api.mocuti.repository.ParticipacaoRepository
import com.api.mocuti.service.ParticipacaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @Operation(
        summary = "Listar participações de um usuário para feedback",
        description = "Retorna lista de participações de um usuário que podem receber feedback, " +
                "filtrando eventos que ocorreram nos últimos 5 dias."
    )
    @GetMapping("/participacao-comentar/{idUsuario}")
    fun listarParticipacoes(@PathVariable idUsuario: Int): List<ParticipacaoFeedbackDTO> {
        val cincoDiasAtras = LocalDate.now().minusDays(5)
        return participacaoService.listarParticipacoesFiltradasPorUsuario(idUsuario, cincoDiasAtras)
    }

    @Operation(
        summary = "Listar eventos passados de um usuário para feedback",
        description = "Retorna lista de participações de um usuário em eventos passados, " +
                "filtrando eventos que ocorreram há mais de 5 dias."
    )
    @GetMapping("/participacao-passados/{idUsuario}")
    fun listarEventosPassados(@PathVariable idUsuario: Int): List<ParticipacaoFeedbackDTO> {
        val cincoDiasAtras = LocalDate.now().minusDays(5)
        return participacaoService.listarParticipacoesPassadas(idUsuario, cincoDiasAtras)
    }

    @Operation(
        summary = "Inscrever usuário em um evento",
        description = "Inscreve um usuário em um evento com o status de inscrição fornecido."
    )
    @PostMapping("/{idEvento}/inscrever")
    fun inscreverUsuario(
        @PathVariable idEvento: Int,
        @RequestParam idUsuario: Int,
        @RequestParam idStatusInscricao: Int
    ): ResponseEntity<Void> {
        participacaoService.inscreverUsuario(idEvento, idUsuario, idStatusInscricao)
        return ResponseEntity.status(201).build()
    }

    @Operation(
        summary = "Cancelar inscrição de um usuário em um evento",
        description = "Cancela a inscrição de um usuário em um evento específico."
    )
    @DeleteMapping("/{idEvento}/cancelar-inscricao")
    fun cancelarInscricao(
        @PathVariable idEvento: Int,
        @RequestParam idUsuario: Int
    ): ResponseEntity<Void> {
        participacaoService.cancelarInscricao(idEvento, idUsuario)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Listar eventos em que o usuário está inscrito",
        description = "Retorna uma lista de eventos nos quais o usuário está inscrito."
    )
    @GetMapping("/eventos-inscritos/{idUsuario}")
    fun listarEventosInscritos(@PathVariable idUsuario: Int): ResponseEntity<List<Evento>> {
        val eventos = participacaoService.listarEventosInscritos(idUsuario)
        return if (eventos.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(eventos)
    }

    @Operation(
        summary = "Listar usuários inscritos com cargo 2 (Usuário) e inscrição pendente",
        description = "Retorna lista de usuários na view 'usuarios_inscritos_cargo2' para um evento específico."
    )
    @GetMapping("/inscritos/cargo2/pendente/{idEvento}")
    fun listarInscritosRestrito(@PathVariable idEvento: Int): ResponseEntity<List<UsuariosInscritosCargo2DTO>> {
        val usuarios = participacaoService.listarUsuariosInscritosRestrito(idEvento)

        return if (usuarios.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(usuarios)
        }
    }

    @Operation(
        summary = "Registrar ou remover a presença de vários usuários em um evento",
        description = "Recebe uma lista de {idUsuario, presente} e atualiza as participações em massa para o evento."
    )
    @PutMapping("/{idEvento}/presenca/bulk")
    fun registrarPresenca(
        @PathVariable idEvento: Int,
        @RequestBody request: BulkPresencaRequest
    ): ResponseEntity<Map<String, Int>> {
        val totalAtualizado = participacaoService.registrarPresenca(
            idEvento,
            request.listaPresenca
        )
        return ResponseEntity.ok(mapOf("totalAtualizado" to totalAtualizado))
    }

    @Operation(
        summary = "Contar usuários inscritos com cargo 2 (Usuário)",
        description = "Retorna a quantidade de usuários inscritos com cargo 2 para um evento específico."
    )
    @GetMapping("/inscritos/cargo2/contagem/{idEvento}")
    fun contarInscritosCargo2(@PathVariable idEvento: Int): ResponseEntity<Map<String, Long>> {
        val count = participacaoService.contarUsuariosInscritosCargo2(idEvento)
        return ResponseEntity.ok(mapOf("quantidade" to count))
    }


    @Operation(
        summary = "Listar convidados de um evento",
        description = "Retorna a lista de convidados (cargo 3) para um evento específico."
    )
    @GetMapping("/convidados/{idEvento}")
    fun listarConvidadosPorEvento(@PathVariable idEvento: Int): ResponseEntity<List<ConvidadoEventoDTO>> {
        val convidados = participacaoService.listarConvidadosPorEvento(idEvento)
        return if (convidados.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(convidados)
        }
    }

    @Operation(
        summary = "Listar eventos confirmados para um usuário",
        description = "Retorna a lista de eventos onde o usuário confirmou presença."
    )
    @GetMapping("/usuario/{usuarioId}")
    fun getEventosPresentes(@PathVariable usuarioId: Int) = participacaoService.listarEventosConfirmados(usuarioId)

    @Operation(
        summary = "Atualizar status de presença de uma participação",
        description = "Atualiza o status de presença de uma participação específica."
    )
    @PatchMapping("/atualizar")
    fun atualizarPresenca(@RequestBody request: AtualizarPresencaRequest): ResponseEntity<String> {
        val atualizado = participacaoService.atualizarStatusParticipacao(request)

        return if (atualizado) {
            ResponseEntity.ok("Status atualizado com sucesso.")
        } else {
            ResponseEntity.status(404).body("Participação não encontrada.")
        }
    }
}

