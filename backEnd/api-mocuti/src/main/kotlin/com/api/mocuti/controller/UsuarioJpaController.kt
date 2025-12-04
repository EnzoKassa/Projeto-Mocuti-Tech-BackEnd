package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Usuario
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus


@RestController
@CrossOrigin(origins = arrayOf("http://localhost:5173")) // libera apenas para o front
@Tag(name = "Usuário", description = "Operações relacionadas a usuários")
@RequestMapping("/usuarios")
class UsuarioJpaController(
    private val usuarioService: UsuarioService
) {

    class EmailNaoEncontradoException(message: String) : RuntimeException(message)
    class SenhaIncorretaException(message: String) : RuntimeException(message)


    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista de todos os usuários cadastrados no sistema"
    )
    @GetMapping("/listar")
    fun listarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.listarTodos()
        return if (usuarios.isNotEmpty()) ResponseEntity.ok(usuarios)
        else ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Listar usuários por cargo",
        description = "Retorna uma lista de usuários filtrados pelo cargo informado"
    )
    @GetMapping("/listar-por-cargo/{cargo}")
    fun listarPorCargo(@PathVariable cargo: Int): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.listarPorCargo(cargo)
        return if (usuarios.isNotEmpty()) ResponseEntity.ok(usuarios)
        else ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Cadastrar um novo usuário",
        description = "Cria e persiste um novo usuário no banco de dados"
    )
    @PostMapping("/cadastrar")
    fun cadastrar(@RequestBody request: UsuarioCadastroRequest): ResponseEntity<Usuario> {
        val usuario = usuarioService.cadastrarUsuario(request)
        return ResponseEntity.status(201).body(usuario)
    }

    @Operation(
        summary = "Login do usuário",
        description = "Autentica um usuário com email e senha fornecidos"
    )
    @PatchMapping("/login")
    fun logar(@RequestBody usuarioLoginRequest: UsuarioLoginRequest): ResponseEntity<Any> {
        return try {
            val usuarioAtualizado = usuarioService.autenticarUsuario(usuarioLoginRequest)

            val response = UsuarioLoginDTO(
                idUsuario = usuarioAtualizado.idUsuario.toLong(),
                nomeCompleto = usuarioAtualizado.nomeCompleto,
                email = usuarioAtualizado.email,
                cargo = usuarioAtualizado.cargo
            )

            ResponseEntity.ok(response)

        } catch (e: UsuarioService.EmailNaoEncontradoException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("erro" to e.message)) // 404 -> email não cadastrado

        } catch (e: UsuarioService.SenhaIncorretaException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("erro" to e.message)) // 401 -> senha incorreta

        } catch (e: Exception) {
            e.printStackTrace() // Mostra erro completo no console
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("erro" to "Erro inesperado: ${e.message}"))
        }
    }

    @Operation(
        summary = "Logout do usuário",
        description = "Desautentica um usuário com base nas informações fornecidas"
    )
    @PatchMapping("/deslogar/{idUsuario}")
    fun deslogar(@RequestBody usuarioLoginRequest: UsuarioLoginRequest): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.desautenticarUsuario(usuarioLoginRequest)
        return ResponseEntity.ok(usuarioAtualizado)
    }

    @Operation(
        summary = "Relatório de usuários",
        description = "Gera um relatório detalhado de usuários cadastrados no sistema"
    )
    @GetMapping("/relatorioUsuarios")
    fun getRelatorioUsuarios(): ResponseEntity<UsuarioRelatorioUsuarios> {
        return ResponseEntity.ok(usuarioService.getRelatorioUsuarios())
    }

    @Operation(
        summary = "Relatório por gênero",
        description = "Gera um relatório com a contagem de usuários por gênero"
    )
    @GetMapping("/relatorioGenero")
    fun relatorioGenero(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(usuarioService.relatorioGenero())
    }

    @Operation(
        summary = "Redefinir senha do usuário",
        description = "Permite a redefinição da senha de um usuário com base no ID fornecido"
    )
    @PatchMapping("/redefinirSenha/{idUsuario}")
    fun redefinirSenha(
        @PathVariable idUsuario: Int,
        @Valid @RequestBody request: UsuarioRedefinirSenhaRequest
    ): ResponseEntity<Void> {
        usuarioService.redefinirSenha(idUsuario, request)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Desativar usuário",
        description = "Desativa a conta do usuário com base no ID fornecido"
    )
    @PatchMapping("/desativar/{idUsuario}")
    fun desativarUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        return ResponseEntity.ok(usuarioService.desativarUsuario(idUsuario))
    }

    @Operation(
        summary = "Ativar usuário",
        description = "Ativa a conta do usuário com base no ID fornecido"
    )
    @PatchMapping("/ativar/{idUsuario}")
    fun ativarUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        return ResponseEntity.ok(usuarioService.ativarUsuario(idUsuario))
    }

    @Operation(
        summary = "Listar visão geral de usuários",
        description = "Fornece uma visão geral dos usuários cadastrados no sistema"
    )
    @GetMapping("/view/visao-geral")
    fun getVisaoGeral(): ResponseEntity<VisaoGeralUsuariosRequest> {
        return ResponseEntity.ok(usuarioService.getVisaoGeralUsuarios())
    }

    @Operation(
        summary = "Listar inscrições por mês durante o ano",
        description = "Fornece o número de inscrições de usuários por mês ao longo do ano"
    )
    @GetMapping("/view/inscricoes-mes-durante-ano")
    fun getInscricoes(): ResponseEntity<List<InscricoesMesDuranteAnoRequest>> {
        return ResponseEntity.ok(usuarioService.getIncricoesMesDuranteAno())
    }

    @Operation(
        summary = "Listar público alvo por gênero",
        description = "Fornece a distribuição do público alvo dos usuários por gênero"
    )
    @GetMapping("/view/publico-alvo-genero")
    fun getPublicoAlvo(): ResponseEntity<List<PublicoAlvoGeneroRequest>> {
        return ResponseEntity.ok(usuarioService.getPublicoAlvoGenero())
    }

    @Operation(
        summary = "Listar faixa etária dos usuários ativos",
        description = "Fornece a distribuição da faixa etária dos usuários que estão ativos no sistema"
    )
    @GetMapping("/view/faixa-etaria-usuarios-ativos")
    fun getFaixaEtariaUsuariosAtivos(): ResponseEntity<List<FaixaEtariaUsuariosAtivosRequest>> {
        return ResponseEntity.ok(usuarioService.getFaixaEtariaUsuariosAtivos())
    }

    @Operation(
        summary = "Listar um usuário por ID",
        description = "Retorna os detalhes de um usuário específico com base no ID fornecido"
    )
    @GetMapping("/listar/{idUsuario}")
    fun listarUmUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        return try {
            val usuario = usuarioService.buscarUsuarioPorId(idUsuario)
            ResponseEntity.ok(usuario)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Editar usuário por ID",
        description = "Atualiza as informações de um usuário existente com base no ID fornecido"
    )
    @PutMapping("/editar/{id}")
    fun editarUsuario(
        @PathVariable id: Long,
        @RequestBody usuarioRequest: EditarUsuarioRequest
    ): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.editarUsuario(id, usuarioRequest)
        return ResponseEntity.ok(usuarioAtualizado)
    }

    @Operation(
        summary = "Verificar existência de email",
        description = "Verifica se um email já está cadastrado no sistema"
    )
    @GetMapping("/existeEmail")
    fun existeEmail(@RequestParam email: String): ResponseEntity<Boolean> {
        val existe = usuarioService.existeEmail(email)
        return ResponseEntity.ok(existe)
    }

    @Operation(
        summary = "Listar lista de presença por evento",
        description = "Retorna a lista de presença dos usuários para um evento específico"
    )
    @GetMapping("/{id}/lista-presenca")
    fun listarPresencaPorEvento(@PathVariable id: Long): Any {
        val resultado = usuarioService.buscarPorEvento(id)
        return resultado ?: mapOf("mensagem" to "Evento não encontrado ou sem participantes")
    }

    @Operation(
        summary = "Atualizar cargo do usuário",
        description = "Atualiza o cargo de um usuário específico com base nos IDs fornecidos"
    )
    @PatchMapping("/{idUsuario}/cargo/{idCargo}")
    fun atualizarCargo(
        @PathVariable idUsuario: Int,
        @PathVariable idCargo: Int
    ): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.atualizarCargo(idUsuario, idCargo)
        return ResponseEntity.ok(usuarioAtualizado)
    }

    @Operation(
        summary = "Listar usuários inativos por cargo",
        description = "Retorna uma lista de usuários inativos filtrados pelo cargo informado"
    )
    @GetMapping("/inativos-por-cargo/{cargoId}")
    fun listarUsuariosInativosPorCargo(@PathVariable cargoId: Int): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.listarUsuariosInativosPorCargo(cargoId)
        return ResponseEntity.ok(usuarios)
    }

}
