import com.api.mocuti.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import com.api.mocuti.entity.Usuario
import com.api.mocuti.controller.UsuarioJpaController
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.util.Optional

class UsuarioJpaControllerTest {
    private val repositorio = mock(UsuarioRepository::class.java)
    private val controller = UsuarioJpaController(repositorio)

    @Test
    fun `deve listar todos os usuarios`() {
        // Cenário 1: Listar todos os usuários cadastrados.
        val usuarios = listOf(
            Usuario(
                idUsuario = 1,
                nomeCompleto = "Teste",
                cpf = "123.456.789-00",
                telefone = "99999-9999",
                email = "teste@email.com",
                dataNascimento = LocalDate.of(1990, 1, 1),
                genero = "Masculino",
                senha = "senha123",
                isAutenticado = false,
                isAtivo = true,
                dtDesativacao = null,
                fkCargo = 2,
                EnderecoUsuario = null,
                fkCanalComunicacaoUsuario = null
            )
        )
        `when`(repositorio.findAll()).thenReturn(usuarios)

        val response = controller.listarTodos()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarios, response.body)
    }

    @Test
    fun `deve listar usuarios por cargo`() {
        // Cenário 2: Listar usuários por cargo específico.
        val cargoInt = 1
        val usuarios = listOf(
            Usuario(
                idUsuario = 1,
                nomeCompleto = "Teste",
                cpf = "123.456.789-00",
                telefone = "99999-9999",
                email = "teste@email.com",
                dataNascimento = LocalDate.of(1990, 1, 1),
                genero = "Masculino",
                senha = "senha123",
                isAutenticado = false,
                isAtivo = true,
                dtDesativacao = null,
                fkCargo = cargoInt,
                EnderecoUsuario = null,
                fkCanalComunicacaoUsuario = null
            )
        )
        `when`(repositorio.findByfkCargo(cargoInt)).thenReturn(usuarios)

        val response = controller.listarPorCargo(cargoInt)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarios, response.body)
    }

    @Test
    fun `deve retornar erro ao listar usuarios por cargo inexistente`() {
        // Cenário 3: Tentar listar usuários de um cargo inexistente.
        val cargoInt = 99
        `when`(repositorio.findByfkCargo(cargoInt)).thenReturn(emptyList())

        val response = controller.listarPorCargo(cargoInt)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertTrue(response.body!!.isEmpty())
    }

    @Test
    fun `deve cadastrar usuario com sucesso`() {
        // Cenário 4: Cadastrar um novo usuário com dados válidos.
        val usuario = Usuario(
            idUsuario = null,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "99999-9999",
            email = "teste@email.com",
            dataNascimento = LocalDate.of(1990, 1, 1),
            genero = "Masculino",
            senha = "senha123",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            fkCargo = 2,
            EnderecoUsuario = null,
            fkCanalComunicacaoUsuario = null
        )

        `when`(repositorio.existsByEmail(usuario.email)).thenReturn(false)
        `when`(repositorio.existsByCpf(usuario.cpf)).thenReturn(false)
        `when`(repositorio.save(usuario)).thenReturn(usuario)

        val response = controller.cadastrar(usuario)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(usuario, response.body)
    }

    @Test
    fun `deve retornar erro ao cadastrar usuario com email ja cadastrado`() {
        // Cenário 5: Tentar cadastrar um usuário com e-mail já existente.
        val usuario = Usuario(
            idUsuario = null,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "99999-9999",
            email = "teste@email.com",
            dataNascimento = LocalDate.of(1990, 1, 1),
            genero = "Masculino",
            senha = "senha123",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            fkCargo = 2,
            EnderecoUsuario = null,
            fkCanalComunicacaoUsuario = null
        )

        `when`(repositorio.existsByEmail(usuario.email)).thenReturn(true)

        val response = controller.cadastrar(usuario)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("E-mail já cadastrado", response.body)
    }

    @Test
    fun `deve redefinir senha com sucesso`() {
        // Cenário 6: Redefinir a senha de um usuário existente.
        val idUsuario = 1
        val novaSenha = mapOf("senha" to "novaSenha123")
        val usuario = Usuario(
            idUsuario = idUsuario,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "99999-9999",
            email = "teste@email.com",
            dataNascimento = LocalDate.of(1990, 1, 1),
            genero = "Masculino",
            senha = "senhaAntiga",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            fkCargo = 2,
            EnderecoUsuario = null,
            fkCanalComunicacaoUsuario = null
        )

        `when`(repositorio.findById(idUsuario)).thenReturn(Optional.of(usuario))
        `when`(repositorio.save(any(Usuario::class.java))).thenReturn(usuario)

        val response = controller.redefinirSenha(idUsuario, novaSenha)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Senha redefinida com sucesso", response.body)
        assertEquals("novaSenha123", usuario.senha)
    }

    @Test
    fun `deve retornar erro ao redefinir senha sem fornecer nova senha`() {
        // Cenário 7: Tentar redefinir a senha sem fornecer a nova senha.
        val idUsuario = 1
        val usuario = Usuario(
            idUsuario = idUsuario,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "99999-9999",
            email = "teste@email.com",
            dataNascimento = LocalDate.of(1990, 1, 1),
            genero = "Masculino",
            senha = "senhaAntiga",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            fkCargo = 2,
            EnderecoUsuario = null,
            fkCanalComunicacaoUsuario = null
        )

        `when`(repositorio.findById(idUsuario)).thenReturn(Optional.of(usuario))

        val response = controller.redefinirSenha(idUsuario, emptyMap())

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Senha não fornecida", response.body)
    }

    @Test
    fun `deve retornar erro ao redefinir senha de usuario nao encontrado`() {
        // Cenário 8: Tentar redefinir a senha de um usuário inexistente.
        val idUsuario = 99
        val novaSenha = mapOf("senha" to "novaSenha123")

        `when`(repositorio.findById(idUsuario)).thenReturn(Optional.empty())

        val response = controller.redefinirSenha(idUsuario, novaSenha)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Usuário não encontrado", response.body)
    }
}