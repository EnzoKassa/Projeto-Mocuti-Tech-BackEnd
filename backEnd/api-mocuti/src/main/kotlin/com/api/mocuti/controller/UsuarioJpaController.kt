package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Usuario
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

@RestController
@CrossOrigin(origins = arrayOf("http://localhost:5173")) // libera apenas para o front
@Tag(name = "Usuário", description = "Operações relacionadas a usuários")
@RequestMapping("/usuarios")
class UsuarioJpaController(
    private val usuarioService: UsuarioService
) {

    @Operation(summary = "Listar todos os usuários")
    @GetMapping("/listar")
    fun listarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.listarTodos()
        return if (usuarios.isNotEmpty()) ResponseEntity.ok(usuarios)
        else ResponseEntity.noContent().build()
    }

    @Operation(summary = "Listar usuários por cargo")
    @GetMapping("/listar-por-cargo/{cargo}")
    fun listarPorCargo(@PathVariable cargo: Int): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.listarPorCargo(cargo)
        return if (usuarios.isNotEmpty()) ResponseEntity.ok(usuarios)
        else ResponseEntity.noContent().build()
    }

    @Operation(summary = "Cadastrar um novo usuário")
    @PostMapping("/cadastrar")
    fun cadastrar(@RequestBody request: UsuarioCadastroRequest): ResponseEntity<Usuario> {
        val usuario = usuarioService.cadastrarUsuario(request)
        return ResponseEntity.status(201).body(usuario)
    }

    @Operation(summary = "Login do usuário")
    @PatchMapping("/login")
    fun logar(@RequestBody usuarioLoginRequest: UsuarioLoginRequest): ResponseEntity<UsuarioLoginDTO> {
        val usuarioAtualizado = usuarioService.autenticarUsuario(usuarioLoginRequest)
        val response = UsuarioLoginDTO(
            idUsuario = usuarioAtualizado.idUsuario.toLong(),
            nomeCompleto = usuarioAtualizado.nomeCompleto,
            email = usuarioAtualizado.email,
            cargo = usuarioAtualizado.cargo
        )
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Logout do usuário")
    @PatchMapping("/deslogar/{idUsuario}")
    fun deslogar(@RequestBody usuarioLoginRequest: UsuarioLoginRequest): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.desautenticarUsuario(usuarioLoginRequest)
        return ResponseEntity.ok(usuarioAtualizado)
    }

    @Operation(summary = "Relatório de usuários")
    @GetMapping("/relatorioUsuarios")
    fun getRelatorioUsuarios(): ResponseEntity<UsuarioRelatorioUsuarios> {
        return ResponseEntity.ok(usuarioService.getRelatorioUsuarios())
    }

    @Operation(summary = "Relatório por gênero")
    @GetMapping("/relatorioGenero")
    fun relatorioGenero(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(usuarioService.relatorioGenero())
    }

    @Operation(summary = "Redefinir senha do usuário")
    @PatchMapping("/redefinirSenha/{idUsuario}")
    fun redefinirSenha(
        @PathVariable idUsuario: Int,
        @Valid @RequestBody request: UsuarioRedefinirSenhaRequest
    ): ResponseEntity<Void> {
        usuarioService.redefinirSenha(idUsuario, request)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Desativar usuário")
    @PatchMapping("/desativar/{idUsuario}")
    fun desativarUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        return ResponseEntity.ok(usuarioService.desativarUsuario(idUsuario))
    }

    @Operation(summary = "Ativar usuário")
    @PatchMapping("/ativar/{idUsuario}")
    fun ativarUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        return ResponseEntity.ok(usuarioService.ativarUsuario(idUsuario))
    }

    @Operation(summary = "Listar visão geral de usuários")
    @GetMapping("/view/visao-geral")
    fun getVisaoGeral(): ResponseEntity<VisaoGeralUsuariosRequest> {
        return ResponseEntity.ok(usuarioService.getVisaoGeralUsuarios())
    }

    @Operation(summary = "Listar inscrições por mês durante o ano")
    @GetMapping("/view/inscricoes-mes-durante-ano")
    fun getInscricoes(): ResponseEntity<List<InscricoesMesDuranteAnoRequest>> {
        return ResponseEntity.ok(usuarioService.getIncricoesMesDuranteAno())
    }

    @Operation(summary = "Listar público alvo por gênero")
    @GetMapping("/view/publico-alvo-genero")
    fun getPublicoAlvo(): ResponseEntity<List<PublicoAlvoGeneroRequest>> {
        return ResponseEntity.ok(usuarioService.getPublicoAlvoGenero())
    }

    @Operation(summary = "Listar faixa etária dos usuários ativos")
    @GetMapping("/view/faixa-etaria-usuarios-ativos")
    fun getFaixaEtariaUsuariosAtivos(): ResponseEntity<List<FaixaEtariaUsuariosAtivosRequest>> {
        return ResponseEntity.ok(usuarioService.getFaixaEtariaUsuariosAtivos())
    }
}
