package mocuti01.controller

import jakarta.validation.Valid
import mocuti01.dto.CadastroUsuarioRequest
import mocuti01.dto.LoginRequest
import mocuti01.dto.RelatorioUsuarios
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import mocuti01.entity.Usuario
import mocuti01.repository.UsuarioRepository


@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController(val repositorio: UsuarioRepository) {

    @GetMapping("/listar")
    fun listarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findAll()
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/listar-por-cargo/{cargoInt}")
    fun listarPorCargo(@PathVariable cargoInt: Int): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findByCargoInt(cargoInt)
        return if (usuarios!!.isNotEmpty()) {
            ResponseEntity.ok(usuarios)
        } else {
            ResponseEntity.status(404).body(emptyList())
        }
    }

    @PostMapping("/cadastrar")
    fun cadastrar(@RequestBody @Valid novoUsuario: Usuario): ResponseEntity<Any> {
        if (repositorio.existsByEmail(novoUsuario.email)) {
            return ResponseEntity.status(400).body("E-mail já cadastrado")
        }
        val usuarioSalvo = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
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

        if (request.cargoInt !in 1..3) {
            return ResponseEntity.status(400).body("Cargo inválido. Deve ser 1, 2 ou 3.")
        }

        val novoUsuario = Usuario(
            nomeCompleto = request.nomeCompleto,
            cpf = request.cpf,
            telefone = request.telefone,
            dataNascimento = request.dataNascimento,
            genero = request.genero,
            email = request.email,
            senha = request.senha,
            cargoInt = request.cargoInt
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

    @GetMapping ("/relatorioGenero")
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
            usuarioAtualizado.senha = novaSenha["senha"] ?: return ResponseEntity.status(400).body("Senha não fornecida")
            repositorio.save(usuarioAtualizado)
            ResponseEntity.status(200).body("Senha redefinida com sucesso")
        } else {
            ResponseEntity.status(404).body("Usuário não encontrado")
        }
    }
}
