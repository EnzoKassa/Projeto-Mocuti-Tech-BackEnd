package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

@RestController
@Tag(name = "Usuário", description = "Operações relacionadas a usuários")
@RequestMapping("/usuarios")
class UsuarioJpaController(
    val repositorio: UsuarioRepository,
    val cargoRepository: CargoRepository,
    val enderecoRepository: EnderecoRepository,
    val canalComunicacaoRepository: CanalComunicacaoRepository,
    val usuarioService: UsuarioService
) {

    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista com todos os usuários cadastrados"
    )
    @GetMapping("/listar")
    fun listarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findAll()
        return if (usuarios.isNotEmpty()) {
            ResponseEntity.status(200).body(usuarios)
        } else {
            ResponseEntity.status(204).build()
        }
    }

    @Operation(
        summary = "Listar usuários por cargo",
        description = "Retorna uma lista de usuários filtrada pelo cargo informado"
    )
    @GetMapping("/listar-por-cargo/{cargo}")
    fun listarPorCargo(@PathVariable cargo: Int): ResponseEntity<List<Usuario>> {
        val cargoOptional = cargoRepository.findById(cargo)
        if (cargoOptional.isEmpty) {
            return ResponseEntity.status(404).build()
        }

        val usuarios = repositorio.findByCargo(cargoOptional.get())
        return if (usuarios.isNotEmpty()) {
            ResponseEntity.status(200).body(usuarios)
        } else {
            ResponseEntity.status(204).build()
        }
    }

    @Operation(
        summary = "Cadastrar um novo usuário",
        description = "Cadastra um novo usuário com cargo opcional (assume cargo padrão se não for informado)"
    )
    @PostMapping("/cadastrar")
    fun cadastrar(@RequestBody request: UsuarioCadastroRequest): ResponseEntity<Usuario> {
        val usuario = usuarioService.cadastrarUsuario(request)
        return ResponseEntity.status(201).body(usuario)
    }

    @Operation(
        summary = "Login do usuário",
        description = "Autentica o usuário com base no e-mail e senha fornecidos"
    )
    @PatchMapping("/logar")
    fun logar(@RequestBody usuarioLoginRequest: UsuarioLoginRequest): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.autenticarUsuario(usuarioLoginRequest)
        return ResponseEntity.status(200).body(usuarioAtualizado)
    }

    @Operation(
        summary = "Logout do usuário",
        description = "Desloga o usuário com base no ID fornecido"
    )
    @PatchMapping("/deslogar/{idUsuario}")
    fun deslogar(@RequestBody usuarioLoginRequest: UsuarioLoginRequest): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.desautenticarUsuario(usuarioLoginRequest)
        return ResponseEntity.status(200).body(usuarioAtualizado)
    }

    @Operation(
        summary = "Relatório de usuários",
        description = "Retorna um relatório com totais de usuários ativos e desativados"
    )
    @GetMapping("/relatorioUsuarios")
    fun getRelatorioUsuarios(): ResponseEntity<UsuarioRelatorioUsuarios> {
        val totalAtivos = repositorio.countByIsAtivo(true)
        val totalDesativados = repositorio.countByIsAtivo(false)

        val relatorio = UsuarioRelatorioUsuarios(
            totalAtivos = totalAtivos, totalDesativados = totalDesativados
        )
        return ResponseEntity.status(200).body(relatorio)
    }

    @Operation(
        summary = "Relatório por gênero",
        description = "Retorna um relatório com a contagem de usuários por gênero"
    )
    @GetMapping("/relatorioGenero")
    fun relatorioGenero(): ResponseEntity<Map<String, Long>> {
        val usuarios = repositorio.findAll()
        val totalMasculino = usuarios.count { it.genero == "Masculino" }.toLong()
        val totalFeminino = usuarios.count { it.genero == "Feminino" }.toLong()
        val totalNaoIdentificado = usuarios.count { it.genero == "Prefiro não identificar" }.toLong()

        val resultado = mapOf(
            "Masculino" to totalMasculino,
            "Feminino" to totalFeminino,
            "Prefiro não identificar" to totalNaoIdentificado
        )
        return ResponseEntity.status(200).body(resultado)
    }

    @Operation(
        summary = "Redefinir senha do usuário",
        description = "Permite redefinir a senha do usuário com base no ID informado"
    )
    @PatchMapping("/redefinirSenha/{idUsuario}")
    fun redefinirSenha(
        @PathVariable idUsuario: Int,
        @Valid @RequestBody request: UsuarioRedefinirSenhaRequest
    ): ResponseEntity<Void> {
        usuarioService.redefinirSenha(idUsuario, request)
        return ResponseEntity.status(200).build()
    }

    @Operation(
        summary = "Desativar usuário",
        description = "Define isAtivo como false e define a data de desativação para hoje"
    )
    @PatchMapping("/desativar/{idUsuario}")
    fun desativarUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.desativarUsuario(idUsuario)
        return ResponseEntity.status(200).body(usuarioAtualizado)
    }

    @Operation(
        summary = "Ativar usuário",
        description = "Define isAtivo como true e limpa a data de desativação"
    )
    @PatchMapping("/ativar/{idUsuario}")
    fun ativarUsuario(@PathVariable idUsuario: Int): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.ativarUsuario(idUsuario)
        return ResponseEntity.status(200).body(usuarioAtualizado)
    }

    @Operation(
        summary = "Listar todos os tipos de Usuários e se estão ativos ou inativos",
        description = "Retorna uma lista todos os tipos de Usuários e se estão ativos ou inativos"
    )
    @GetMapping("/view/visao-geral")
    fun get(): ResponseEntity<VisaoGeralUsuariosRequest> {
        val visaoGeral = usuarioService.getVisaoGeralUsuarios()

        return ResponseEntity.status(200).body(visaoGeral)
    }

    @Operation(
        summary = "Listar todas as inscrições por mês durante o ano",
        description = "Retorna uma lista com todas as inscrições por mês durante o ano"
    )
    @GetMapping("/view/inscricoes-mes-durante-ano")
    fun getInscricoes(): ResponseEntity<List<InscricoesMesDuranteAnoRequest>> {
        val inscricao = usuarioService.getIncricoesMesDuranteAno()

        return ResponseEntity.status(200).body(inscricao)
    }

    @Operation(
        summary = "Listar público alvo por gênero",
        description = "Retorna uma lista com o público alvo por gênero"
    )
    @GetMapping("/view/publico-alvo-genero")
    fun getPublicoAlvo(): ResponseEntity<List<PublicoAlvoGeneroRequest>> {
        val publicoAlvo = usuarioService.getPublicoAlvoGenero()

        return ResponseEntity.status(200).body(publicoAlvo)
    }

    @Operation(
        summary = "Listar faixa etária dos usuários ativos",
        description = "Retorna uma lista com a faixa etária dos usuários ativos"
    )
    @GetMapping("/view/faixa-etaria-usuarios-ativos")
    fun getFaixaEtariaUsuariosAtivos(): ResponseEntity<List<FaixaEtariaUsuariosAtivosRequest>> {
        val faixaEtaria = usuarioService.getFaixaEtariaUsuariosAtivos()
        return ResponseEntity.status(200).body(faixaEtaria)
    }
}

