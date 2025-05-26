package com.api.mocuti.controller

import jakarta.validation.Valid
import com.api.mocuti.dto.CadastroUsuarioRequest
import com.api.mocuti.dto.LoginRequest
import com.api.mocuti.dto.RelatorioUsuarios
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.CargoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.UsuarioRepository
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController(val repositorio: UsuarioRepository, val cargoRepository: CargoRepository) {

    @GetMapping("/listar")
    fun listarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findAll()
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/listar-por-cargo/{cargo}")
    fun listarPorCargo(@PathVariable cargo: Int): ResponseEntity<List<Usuario>> {
        val cargoOptional = cargoRepository.findById(cargo)
        if (cargoOptional.isEmpty) {
            return ResponseEntity.status(404).body(emptyList())
        }

        val usuarios = repositorio.findByCargo(cargoOptional.get())
        return if (usuarios.isNotEmpty()) {
            ResponseEntity.ok(usuarios)
        } else {
            ResponseEntity.status(404).body(emptyList())
        }
    }

    @PostMapping("/cadastrar")
    fun cadastrar(usuario: Usuario): ResponseEntity<Any> {
        if (!usuario.email.contains("@")) {
            throw IllegalArgumentException("E-mail inválido")
        }

        if (repositorio.existsByEmail(usuario.email)) {
            return ResponseEntity("E-mail já cadastrado", HttpStatus.BAD_REQUEST)
        }

        if (repositorio.existsByCpf(usuario.cpf)) {
            return ResponseEntity("CPF já cadastrado", HttpStatus.BAD_REQUEST)
        }

        val usuarioSalvo = repositorio.save(usuario)
        return ResponseEntity(usuarioSalvo, HttpStatus.CREATED)
    }

    @PostMapping("/cadastrar-usuario")
    fun cadastroMantenedor(@RequestBody @Valid request: CadastroUsuarioRequest): ResponseEntity<Any> {
        if (repositorio.existsByEmail(request.email)) {
            return ResponseEntity.status(400).body("E-mail já cadastrado")
        }
        if (repositorio.existsByCpf(request.cpf)) {
            return ResponseEntity.status(400).body("CPF já cadastrado")
        }

        if (repositorio.existsByCpf(request.cpf) && repositorio.existsByEmail(request.email)) {
            return ResponseEntity.status(400).body("CPF e E-mail já cadastrados")
        }

        if (request.cargo !in 1..3) {
            return ResponseEntity.status(400).body("Cargo inválido. Deve ser 1, 2 ou 3.")
        }

        val cargoOptional = cargoRepository.findById(request.cargo)
        if (cargoOptional.isEmpty) {
            return ResponseEntity.status(400).body("Cargo não encontrado")
        }

        val cargo = cargoOptional.get()

        val novoUsuario = Usuario(
            nomeCompleto = request.nomeCompleto,
            cpf = request.cpf,
            telefone = request.telefone,
            dataNascimento = request.dataNascimento,
            genero = request.genero,
            email = request.email,
            senha = request.senha,
            cargo = cargo
        )

        val usuarioSalvo = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
    }

    @PatchMapping("/logar/{idUsuario}")
    fun logar(@PathVariable idUsuario: Int, @RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<Any> {
        val usuario = repositorio.findById(idUsuario)
        return if (usuario.isPresent && usuario.get().senha == loginRequest.senha) {
            val usuarioAtualizado = usuario.get()
            usuarioAtualizado.isAutenticado = true
            repositorio.save(usuarioAtualizado)
            ResponseEntity.status(200).body("Login bem-sucedido")
        } else {
            ResponseEntity.status(404).body("Credenciais inválidas ou usuário não encontrado")
        }
    }

    @PatchMapping("/deslogar/{idUsuario}")
    fun deslogar(@PathVariable idUsuario: Int): ResponseEntity<Any> {
        val usuario = repositorio.findById(idUsuario)
        return if (usuario.isPresent) {
            val usuarioAtualizado = usuario.get()
            usuarioAtualizado.isAutenticado = false
            repositorio.save(usuarioAtualizado)
            ResponseEntity.status(200).body("Logout bem-sucedido")
        } else {
            ResponseEntity.status(404).body("Usuário não encontrado")
        }
    }

    @GetMapping("/relatorioUsuarios")
    fun getRelatorioUsuarios(): ResponseEntity<RelatorioUsuarios> {
        val totalAtivos = repositorio.countByIsAtivo(true)
        val totalDesativados = repositorio.countByIsAtivo(false)

        val relatorio = RelatorioUsuarios(
            totalAtivos = totalAtivos, totalDesativados = totalDesativados
        )
        return ResponseEntity.ok(relatorio)
    }

    @GetMapping("/relatorioGenero")
    fun relatorioGenero(): ResponseEntity<Map<String, Long>> {
        val usuarios = repositorio.findAll()
        val totalHomens = usuarios.count { it.genero == "Masculino" }.toLong()
        val totalMulheres = usuarios.count { it.genero == "Feminino" }.toLong()

        val resultado = mapOf(
            "homem" to totalHomens,
            "mulher" to totalMulheres
        )
        return ResponseEntity.ok(resultado)
    }

    @PatchMapping("/redefinirSenha/{idUsuario}")
    fun redefinirSenha(@PathVariable idUsuario: Int, @RequestBody novaSenha: Map<String, String>): ResponseEntity<Any> {
        val usuario = repositorio.findById(idUsuario)
        return if (usuario.isPresent) {
            val usuarioAtualizado = usuario.get()
            usuarioAtualizado.senha =
                novaSenha["senha"] ?: return ResponseEntity.status(400).body("Senha não fornecida")
            repositorio.save(usuarioAtualizado)
            ResponseEntity.status(200).body("Senha redefinida com sucesso")
        } else {
            ResponseEntity.status(404).body("Usuário não encontrado")
        }
    }
}
