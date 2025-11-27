package com.api.mocuti.service

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Preferencia
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val cargoRepository: CargoRepository,
    private val enderecoRepository: EnderecoRepository,
    private val canalComunicacaoRepository: CanalComunicacaoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val preferenciaRepository: PreferenciaRepository,
    private val passwordEncoder: PasswordEncoder
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
        // 🔹 Verificações de e-mail e CPF duplicados
        if (usuarioRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email já cadastrado")
        }

        if (usuarioRepository.existsByCpf(request.cpf)) {
            throw IllegalArgumentException("CPF já cadastrado")
        }

        // 🔹 Validação de idade mínima (5 anos)
        val hoje = LocalDate.now()
        val nascimento = request.dataNascimento
        val idade = hoje.year - nascimento.year - if (hoje < nascimento.plusYears((hoje.year - nascimento.year).toLong())) 1 else 0

        if (idade < 5) {
            throw IllegalArgumentException("Data de aniversário inválida: usuário deve ter pelo menos 5 anos")
        }

        // 🔹 Busca do cargo (padrão caso não informado)
        val cargo = if (request.cargo != null) {
            cargoRepository.findById(request.cargo)
                .orElseThrow { IllegalArgumentException("Cargo não encontrado") }
        } else {
            val cargoPadraoId = 2
            cargoRepository.findById(cargoPadraoId)
                .orElseThrow { IllegalArgumentException("Cargo não encontrado") }
        }

        // 🔹 Canal de comunicação
        val canalComunicacao = canalComunicacaoRepository.findById(request.canalComunicacao)
            .orElseThrow { IllegalArgumentException("Canal de comunicação não encontrado") }

        // 🔹 Endereço
        val endereco = enderecoRepository.save(request.endereco)

        // 🔹 Criação do usuário
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

        val usuarioSalvo = usuarioRepository.save(novoUsuario)

        // 2️⃣ Localiza a categoria (já cadastrada no banco)
        val categoria = categoriaRepository.findById(request.idCategoriaPreferida)
            .orElseThrow { RuntimeException("Categoria não encontrada") }

        // 3️⃣ Cria e salva a preferência
        val preferencia = Preferencia(
            usuario = usuarioSalvo,
            categoria = categoria
        )

        preferenciaRepository.save(preferencia)

        // 4️⃣ Retorna resposta formatada
        return usuarioSalvo
    }

    class EmailNaoEncontradoException(message: String) : RuntimeException(message)
    class SenhaIncorretaException(message: String) : RuntimeException(message)


    fun autenticarUsuario(usuarioLoginRequest: UsuarioLoginRequest): Usuario {
        val usuario = usuarioRepository.findByEmail(usuarioLoginRequest.email)
            ?: throw EmailNaoEncontradoException("E-mail não cadastrado") // Mensagem mais clara

        val senhaInput = usuarioLoginRequest.senha
        val senhaCorreta = if (usuario.senha.startsWith("\$2a\$") || usuario.senha.startsWith("\$2b\$") || usuario.senha.startsWith("\$2y\$")) {
            passwordEncoder.matches(senhaInput, usuario.senha)
        } else {
            usuario.senha == senhaInput
        }

        if (!senhaCorreta) throw SenhaIncorretaException("Senha incorreta") // Mensagem clara

        usuario.isAutenticado = true

        // 🔹 Tente salvar, mas se falhar, não bloqueia login
        return try {
            usuarioRepository.save(usuario)
        } catch (e: Exception) {
            println("⚠️ Erro ao salvar isAutenticado: ${e.message}")
            usuario
        }
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

        val senhaBanco = usuario.senha

        val senhaConfere = if (senhaBanco.startsWith("\$2a\$") || senhaBanco.startsWith("\$2b\$") || senhaBanco.startsWith("\$2y\$")) {
            // Senha já criptografada
            passwordEncoder.matches(request.senhaAtual, senhaBanco)
        } else {
            // Senha salva em texto puro
            request.senhaAtual == senhaBanco
        }

        if (!senhaConfere) {
            throw IllegalArgumentException("Senha atual incorreta.")
        }

        // Evita reutilizar senha antiga (funciona tanto com hash quanto texto)
        if (senhaBanco.startsWith("\$2a\$") || senhaBanco.startsWith("\$2b\$") || senhaBanco.startsWith("\$2y\$")) {
            // Já é criptografada → comparar com matches
            if (passwordEncoder.matches(request.novaSenha, senhaBanco)) {
                throw IllegalArgumentException("A nova senha não pode ser igual à anterior.")
            }
        } else {
            // Era texto puro → comparar diretamente
            if (request.novaSenha == senhaBanco) {
                throw IllegalArgumentException("A nova senha não pode ser igual à anterior.")
            }
        }

        // **Sempre criptografa ao salvar**
        usuario.senha = passwordEncoder.encode(request.novaSenha)
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

            // Verifica se já existe outro usuário com o mesmo e-mail
            usuarioRepository.findByEmail(usuarioRequest.email)?.let {
                if (it.idUsuario != id.toInt()) {
                    throw IllegalArgumentException("Já existe um usuário com este e-mail.")
                }
            }

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

    fun existeEmail(email: String): Boolean {
        return usuarioRepository.findByEmail(email) != null
    }


    fun buscarPorEvento(idEvento: Long): ListaPresencaEventoDTO? {
        return usuarioRepository.findByEventoId(idEvento)
    }

    fun atualizarCargo(idUsuario: Int, idCargo: Int): Usuario {
        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { NoSuchElementException("Usuário com ID $idUsuario não encontrado") }

        val cargo = cargoRepository.findById(idCargo)
            .orElseThrow { NoSuchElementException("Cargo com ID $idCargo não encontrado") }

        usuario.cargo = cargo
        return usuarioRepository.save(usuario)
    }

}