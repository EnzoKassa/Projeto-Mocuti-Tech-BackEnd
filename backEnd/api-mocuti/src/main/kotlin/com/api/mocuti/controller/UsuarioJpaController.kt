package com.api.mocuti.controller

import jakarta.validation.Valid
import com.api.mocuti.dto.CadastroUsuarioRequest
import com.api.mocuti.dto.LoginRequest
import com.api.mocuti.dto.RelatorioUsuarios
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.CanalComunicacaoRepository
import com.api.mocuti.repository.CargoRepository
import com.api.mocuti.repository.EnderecoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.UsuarioRepository
import org.springframework.http.HttpStatus
import kotlin.text.get
import kotlin.text.toLong

@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController(
    val repositorio: UsuarioRepository,
    val cargoRepository: CargoRepository,
    val enderecoRepository: EnderecoRepository,
    val canalComunicacaoRepository: CanalComunicacaoRepository
) {

    @GetMapping("/listar")
    fun listarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findAll()
        return if (usuarios.isNotEmpty()) {
            ResponseEntity.ok(usuarios)
        } else {
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }
    }

    @GetMapping("/listar-por-cargo/{cargo}")
    fun listarPorCargo(@PathVariable cargo: Int): ResponseEntity<List<Usuario>> {
        val cargoOptional = cargoRepository.findById(cargo)
        if (cargoOptional.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

        val usuarios = repositorio.findByCargo(cargoOptional.get())
        return if (usuarios.isNotEmpty()) {
            ResponseEntity.ok(usuarios)
        } else {
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }
    }

    @PostMapping("/cadastrar")
    fun cadastrar(@RequestBody @Valid usuario: Usuario): ResponseEntity<Usuario> {
        return try {
            if (!usuario.email.contains("@")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }

            if (repositorio.existsByEmail(usuario.email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }

            if (repositorio.existsByCpf(usuario.cpf)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }

            val usuarioSalvo = repositorio.save(usuario)
            ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/cadastrar-usuario")
    fun cadastroMantenedor(@RequestBody @Valid request: CadastroUsuarioRequest): ResponseEntity<Usuario> {
        if (repositorio.existsByEmail(request.email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
        if (repositorio.existsByCpf(request.cpf)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        val cargoOptional = cargoRepository.findById(request.cargo)
        if (cargoOptional.isEmpty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        val enderecoOptional = enderecoRepository.findById(request.endereco)
        if (enderecoOptional.isEmpty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        val canalComunicacaoOptional = canalComunicacaoRepository.findById(request.canalComunicacao)
        if (canalComunicacaoOptional.isEmpty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        val novoUsuario = Usuario(
            idUsuario = 0,
            nomeCompleto = request.nomeCompleto,
            cpf = request.cpf,
            telefone = request.telefone,
            email = request.email,
            dt_nasc = request.dataNascimento,
            genero = request.genero,
            senha = request.senha,
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            cargo = cargoOptional.get(),
            endereco = enderecoOptional.get(),
            canalComunicacao = canalComunicacaoOptional.get()
        )

        val usuarioSalvo = repositorio.save(novoUsuario)
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo)
    }

    @PatchMapping("/logar/{idUsuario}")
    fun logar(@PathVariable idUsuario: Int, @RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<String> {
        val usuario = repositorio.findById(idUsuario)
        return if (usuario.isPresent && usuario.get().senha == loginRequest.senha) {
            val usuarioAtualizado = usuario.get()
            usuarioAtualizado.isAutenticado = true
            repositorio.save(usuarioAtualizado)
            ResponseEntity.ok("Login bem-sucedido")
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas ou usuário não encontrado")
        }
    }

    @PatchMapping("/deslogar/{idUsuario}")
    fun deslogar(@PathVariable idUsuario: Int): ResponseEntity<String> {
        val usuario = repositorio.findById(idUsuario)
        return if (usuario.isPresent) {
            val usuarioAtualizado = usuario.get()
            usuarioAtualizado.isAutenticado = false
            repositorio.save(usuarioAtualizado)
            ResponseEntity.ok("Logout bem-sucedido")
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado")
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
    fun redefinirSenha(@PathVariable idUsuario: Int, @RequestBody novaSenha: Map<String, String>): ResponseEntity<String> {
        val usuario = repositorio.findById(idUsuario)
        return if (usuario.isPresent) {
            val usuarioAtualizado = usuario.get()
            usuarioAtualizado.senha =
                novaSenha["senha"] ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha não fornecida")
            repositorio.save(usuarioAtualizado)
            ResponseEntity.ok("Senha redefinida com sucesso")
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado")
        }
    }
}
