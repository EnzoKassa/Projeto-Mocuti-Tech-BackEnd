package com.api.mocuti.service

import com.api.mocuti.dto.UsuarioLoginRequest
import com.api.mocuti.dto.UsuarioCadastroRequest
import com.api.mocuti.dto.UsuarioRedefinirSenhaRequest
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UsuarioService(
    val usuarioRepository: UsuarioRepository,
    val cargoRepository: CargoRepository,
    val enderecoRepository: EnderecoRepository,
    val canalComunicacaoRepository: CanalComunicacaoRepository
) {
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

        val endereco = enderecoRepository.findById(request.endereco)
            .orElseThrow { IllegalArgumentException("Endereço não encontrado") }

        val canalComunicacao = canalComunicacaoRepository.findById(request.canalComunicacao)
            .orElseThrow { IllegalArgumentException("Canal de comunicação não encontrado") }

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

}