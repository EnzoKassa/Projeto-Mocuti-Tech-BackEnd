package com.api.mocuti.service

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val cargoRepository: CargoRepository,
    private val enderecoRepository: EnderecoRepository,
    private val canalComunicacaoRepository: CanalComunicacaoRepository
) {
    fun listarTodos(): List<Usuario> = usuarioRepository.findAll()

    fun listarPorCargo(cargoId: Int): List<Usuario> {
        val cargo = cargoRepository.findById(cargoId)
            .orElseThrow { IllegalArgumentException("Cargo não encontrado") }
        return usuarioRepository.findByCargo(cargo)
    }

    fun getRelatorioUsuarios(): UsuarioRelatorioUsuarios {
        val totalAtivos = usuarioRepository.countByIsAtivo(true)
        val totalDesativados = usuarioRepository.countByIsAtivo(false)
        return UsuarioRelatorioUsuarios(totalAtivos, totalDesativados)
    }

    fun relatorioGenero(): Map<String, Long> {
        val usuarios = usuarioRepository.findAll()
        val totalMasculino = usuarios.count { it.genero == "Masculino" }.toLong()
        val totalFeminino = usuarios.count { it.genero == "Feminino" }.toLong()
        val totalNaoIdentificado = usuarios.count { it.genero == "Prefiro não identificar" }.toLong()

        return mapOf(
            "Masculino" to totalMasculino,
            "Feminino" to totalFeminino,
            "Prefiro não identificar" to totalNaoIdentificado
        )
    }
    
    fun cadastrarUsuario(request: UsuarioCadastroRequest): Usuario {
        if (usuarioRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email já cadastrado")
        }

        if (usuarioRepository.existsByCpf(request.cpf)) {
            throw IllegalArgumentException("CPF já cadastrado")
        }

        val cargo = if (request.cargo != null) {
            cargoRepository.findById(request.cargo)
                .orElseThrow { IllegalArgumentException("Cargo não encontrado") }
        } else {
            // Defina aqui o ID do cargo padrão
            val cargoPadraoId = 2
            cargoRepository.findById(cargoPadraoId)
                .orElseThrow { IllegalArgumentException("Cargo não encontrado") }
        }

        val canalComunicacao = canalComunicacaoRepository.findById(request.canalComunicacao)
            .orElseThrow { IllegalArgumentException("Canal de comunicação não encontrado") }

        val endereco = enderecoRepository.save(request.endereco)

        val novoUsuario = Usuario(
            idUsuario = 0,
            nomeCompleto = request.nomeCompleto,
            cpf = request.cpf,
            telefone = request.telefone,
            email = request.email,
            dt_nasc = request.dataNascimento,
            etnia = request.etnia,
            nacionalidade = request.nacionalidade,
            genero = request.genero,
            senha = request.senha,
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            cargo = cargo,
            endereco = endereco,
            canalComunicacao = canalComunicacao
        )

        return usuarioRepository.save(novoUsuario)
    }

    fun autenticarUsuario(usuarioLoginRequest: UsuarioLoginRequest): Usuario {
        val usuario = usuarioRepository.findByEmail(usuarioLoginRequest.email)
            ?: throw IllegalArgumentException("Usuário não encontrado com este e-mail")

        if (usuario.senha != usuarioLoginRequest.senha) {
            throw IllegalArgumentException("Senha incorreta")
        }

        usuario.isAutenticado = true
        return usuarioRepository.save(usuario)
    }

    fun desautenticarUsuario(usuarioLoginRequest: UsuarioLoginRequest): Usuario {
        val usuario = usuarioRepository.findByEmail(usuarioLoginRequest.email)
            ?: throw IllegalArgumentException("Usuário não encontrado com este e-mail")

        if (usuario.senha != usuarioLoginRequest.senha) {
            throw IllegalArgumentException("Senha incorreta")
        }

        usuario.isAutenticado = false
        return usuarioRepository.save(usuario)
    }

    fun redefinirSenha(idUsuario: Int, request: UsuarioRedefinirSenhaRequest) {
        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        usuario.senha = request.senha
        usuarioRepository.save(usuario)
    }

    fun desativarUsuario(idUsuario: Int): Usuario {
        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { NoSuchElementException("Usuário com ID $idUsuario não encontrado") }

        usuario.isAtivo = false
        usuario.dtDesativacao = LocalDate.now()

        return usuarioRepository.save(usuario)
    }

    fun ativarUsuario(idUsuario: Int): Usuario {
        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { NoSuchElementException("Usuário com ID $idUsuario não encontrado") }

        usuario.isAtivo = true
        usuario.dtDesativacao = null

        return usuarioRepository.save(usuario)
    }

    fun getVisaoGeralUsuarios(): VisaoGeralUsuariosRequest {
        return usuarioRepository.buscarVisaoGeralUsuarios()
    }

    fun getIncricoesMesDuranteAno(): List<InscricoesMesDuranteAnoRequest> {
        return usuarioRepository.getInscricoesMesDuranteAno()
    }

    fun getPublicoAlvoGenero(): List<PublicoAlvoGeneroRequest> {
        return usuarioRepository.getPublicoAlvoGenero()
    }

    fun getFaixaEtariaUsuariosAtivos(): List<FaixaEtariaUsuariosAtivosRequest> {
        return usuarioRepository.getFaixaEtariaUsuariosAtivos()
    }
    fun buscarUsuarioPorId(idUsuario: Int): Usuario {
        return usuarioRepository.findById(idUsuario)
            .orElseThrow { NoSuchElementException("Usuário com ID $idUsuario não encontrado") }
    }

    fun editarUsuario(id: Long, usuarioRequest: EditarUsuarioRequest): Usuario {
        val usuario = usuarioRepository.findById(id.toInt())
            .orElseThrow { NoSuchElementException("Usuário não encontrado com ID: $id") }

        usuario.nomeCompleto = usuarioRequest.nomeCompleto
        usuario.cpf = usuarioRequest.cpf
        usuario.telefone = usuarioRequest.telefone
        usuario.email = usuarioRequest.email
        usuario.dt_nasc = try {
            LocalDate.parse(usuarioRequest.dt_nasc)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Data de nascimento inválida. Use o formato yyyy-MM-dd.")
        }
        usuario.etnia = usuarioRequest.etnia
        usuario.nacionalidade = usuarioRequest.nacionalidade
        usuario.genero = usuarioRequest.genero

        return usuarioRepository.save(usuario)
    }

    fun buscarPorEvento(idEvento: Long): ListaPresencaEventoDTO? {
        return usuarioRepository.findByEventoId(idEvento)
    }
}