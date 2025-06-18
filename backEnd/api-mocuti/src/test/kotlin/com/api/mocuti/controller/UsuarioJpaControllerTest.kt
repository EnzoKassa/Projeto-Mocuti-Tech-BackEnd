import com.api.mocuti.controller.UsuarioJpaController
import com.api.mocuti.entity.CanalComunicacao
import org.junit.jupiter.api.Assertions.*
import com.api.mocuti.repository.UsuarioRepository
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import com.api.mocuti.entity.Usuario
import com.api.mocuti.entity.Cargo
import com.api.mocuti.entity.Endereco
import com.api.mocuti.repository.CanalComunicacaoRepository
import com.api.mocuti.repository.CargoRepository
import com.api.mocuti.repository.EnderecoRepository
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import java.time.LocalDate
import java.util.Optional


class UsuarioJpaControllerTest {
    private val repositorio: UsuarioRepository = mock(UsuarioRepository::class.java)
    private val cargoRepository: CargoRepository = mock(CargoRepository::class.java)
    private val enderecoRepository: EnderecoRepository = mock(EnderecoRepository::class.java)
    private val canalComunicacaoRepository: CanalComunicacaoRepository = mock(CanalComunicacaoRepository::class.java)
    private val controller =
        UsuarioJpaController(repositorio, cargoRepository, enderecoRepository, canalComunicacaoRepository)

    private lateinit var cargoTeste: Cargo
    private lateinit var enderecoTeste: Endereco
    private lateinit var comunicacaoTeste: CanalComunicacao
    private lateinit var usuarioTeste: Usuario

    @BeforeEach
    fun setup() {

        cargoTeste = Cargo(
            id_cargo = 1,
            tipoCargo = "Administrador"
        )

        enderecoTeste = Endereco(
            idEndereco = 1,
            CEP = "12345678",
            logradouro = "Rua das Flores",
            numero = 123,
            complemento = "Apto 45",
            UF = "SP",
            estado = "São Paulo",
            bairro = "Centro"
        )

        comunicacaoTeste = CanalComunicacao(
            id = 1,
            tipoCanalComunicacao = "Email"
        )

        usuarioTeste = Usuario(
            idUsuario = 1,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "99999-9999",
            email = "teste@email.com",
            dt_nasc = LocalDate.of(1990, 1, 1),
            genero = "Masculino",
            senha = "senha123",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            cargo = cargoTeste,
            endereco = enderecoTeste,
            canalComunicacao = comunicacaoTeste

        )
    }

    @Test
    fun `deve listar todos os usuarios`() {
        // Cenário 1: Listar todos os usuários cadastrados.

        `when`(repositorio.findAll()).thenReturn(listOf(usuarioTeste))

        val response = controller.listarTodos()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(usuarioTeste), response.body)
    }

    @Test
    fun `deve retornar usuarios quando cargo existe e possui usuarios`() {
        `when`(cargoRepository.findById(cargoTeste.id_cargo)).thenReturn(Optional.of(cargoTeste))
        `when`(repositorio.findByCargo(cargoTeste)).thenReturn(listOf(usuarioTeste))

        val response = controller.listarPorCargo(cargoTeste.id_cargo)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(usuarioTeste), response.body)
    }


    @Test
    fun `deve retornar no content quando cargo existe mas nao possui usuarios`() {
        `when`(cargoRepository.findById(cargoTeste.id_cargo)).thenReturn(Optional.of(cargoTeste))
        `when`(repositorio.findByCargo(cargoTeste)).thenReturn(emptyList())

        val response = controller.listarPorCargo(cargoTeste.id_cargo)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deve retornar not found quando cargo nao existe`() {
        val idCargoInexistente = 999
        `when`(cargoRepository.findById(idCargoInexistente)).thenReturn(Optional.empty())

        val response = controller.listarPorCargo(idCargoInexistente)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(null, response.body)
    }

    @Test
    fun `deve retornar erro ao listar usuarios por cargo inexistente`() {
        val idCargoInexistente = 999
        `when`(cargoRepository.findById(idCargoInexistente)).thenReturn(Optional.empty())

        val response = controller.listarPorCargo(idCargoInexistente)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(null, response.body)
    }

    @Test
    fun `deve cadastrar usuario com sucesso`() {
        // Cenário 4: Cadastrar um novo usuário com dados válidos.

        `when`(repositorio.existsByEmail(usuarioTeste.email)).thenReturn(false)
        `when`(repositorio.existsByCpf(usuarioTeste.cpf)).thenReturn(false)
        `when`(repositorio.save(usuarioTeste)).thenReturn(usuarioTeste)

        val response = controller.cadastrar(usuarioTeste)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    @Test
    fun `deve retornar erro ao cadastrar usuario com email ja cadastrado`() {
        // Cenário 5: Tentar cadastrar um usuário com e-mail já existente.

        `when`(repositorio.existsByEmail(usuarioTeste.email)).thenReturn(true)

        val response = controller.cadastrar(usuarioTeste)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(null, response.body)
    }

    @Test
    fun `deve redefinir senha com sucesso`() {
        // Cenário 6: Redefinir a senha de um usuário existente.
        val usuarioComSenhaAntiga = usuarioTeste.copy(senha = "senhaAntiga")
        val novaSenha = mapOf("senha" to "novaSenha123")

        `when`(repositorio.findById(usuarioTeste.idUsuario)).thenReturn(Optional.of(usuarioTeste))
        `when`(repositorio.save(any())).thenReturn(usuarioTeste)

        val response = controller.redefinirSenha(usuarioTeste.idUsuario, novaSenha)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Senha redefinida com sucesso", response.body)
        assertEquals("novaSenha123", usuarioTeste.senha)
    }

    @Test
    fun `deve retornar erro ao redefinir senha sem fornecer nova senha`() {
        // Cenário 7: Tentar redefinir a senha sem fornecer a nova senha.

        `when`(repositorio.findById(usuarioTeste.idUsuario)).thenReturn(Optional.of(usuarioTeste))

        val response = controller.redefinirSenha(usuarioTeste.idUsuario, emptyMap())

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