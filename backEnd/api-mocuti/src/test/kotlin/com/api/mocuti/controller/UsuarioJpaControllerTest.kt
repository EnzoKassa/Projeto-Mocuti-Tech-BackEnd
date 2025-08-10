import com.api.mocuti.controller.UsuarioJpaController
import com.api.mocuti.dto.*
import com.api.mocuti.entity.*
import com.api.mocuti.repository.*
import com.api.mocuti.service.UsuarioService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*

class UsuarioJpaControllerTest {

    private val repositorio: UsuarioRepository = mock(UsuarioRepository::class.java)
    private val cargoRepository: CargoRepository = mock(CargoRepository::class.java)
    private val enderecoRepository: EnderecoRepository = mock(EnderecoRepository::class.java)
    private val canalComunicacaoRepository: CanalComunicacaoRepository = mock(CanalComunicacaoRepository::class.java)
    private val usuarioService: UsuarioService = mock(UsuarioService::class.java)

    private val controller = UsuarioJpaController(
        repositorio,
        cargoRepository,
        enderecoRepository,
        canalComunicacaoRepository,
        usuarioService
    )

    private lateinit var cargoTeste: Cargo
    private lateinit var enderecoTeste: Endereco
    private lateinit var comunicacaoTeste: CanalComunicacao
    private lateinit var usuarioTeste: Usuario

    @BeforeEach
    fun setup() {
        cargoTeste = Cargo(idCargo = 1, tipoCargo = "Administrador")
        enderecoTeste = Endereco(
            idEndereco = 1,
            cep = "12345678",
            logradouro = "Rua das Flores",
            numero = 123,
            complemento = "Apto 45",
            uf = "SP",
            estado = "São Paulo",
            bairro = "Centro"
        )
        comunicacaoTeste = CanalComunicacao(idCanalComunicacao = 1, tipoCanalComunicacao = "Email")
        usuarioTeste = Usuario(
            idUsuario = 1,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "teste@email.com",
            dt_nasc = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileira",
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
        `when`(repositorio.findAll()).thenReturn(listOf(usuarioTeste))

        val response = controller.listarTodos()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(usuarioTeste), response.body)
    }

    @Test
    fun `deve retornar usuarios quando cargo existe e possui usuarios`() {
        `when`(cargoRepository.findById(cargoTeste.idCargo)).thenReturn(Optional.of(cargoTeste))
        `when`(repositorio.findByCargo(cargoTeste)).thenReturn(listOf(usuarioTeste))

        val response = controller.listarPorCargo(cargoTeste.idCargo)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(usuarioTeste), response.body)
    }

    @Test
    fun `deve retornar no content quando cargo existe mas nao possui usuarios`() {
        `when`(cargoRepository.findById(cargoTeste.idCargo)).thenReturn(Optional.of(cargoTeste))
        `when`(repositorio.findByCargo(cargoTeste)).thenReturn(emptyList())

        val response = controller.listarPorCargo(cargoTeste.idCargo)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deve retornar not found quando cargo nao existe`() {
        val idCargoInexistente = 999
        `when`(cargoRepository.findById(idCargoInexistente)).thenReturn(Optional.empty())

        val response = controller.listarPorCargo(idCargoInexistente)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deve cadastrar usuario com sucesso`() {
        val request = UsuarioCadastroRequest(
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "teste@email.com",
            dataNascimento = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileira",
            genero = "Masculino",
            senha = "senha123",
            cargo = 1,
            endereco = 1,
            canalComunicacao = 1
        )

        `when`(usuarioService.cadastrarUsuario(request)).thenReturn(usuarioTeste)

        val response = controller.cadastrar(request)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    @Test
    fun `deve redefinir senha com sucesso`() {
        val request = UsuarioRedefinirSenhaRequest(senha = "novaSenha123")

        doNothing().`when`(usuarioService).redefinirSenha(usuarioTeste.idUsuario, request)

        val response = controller.redefinirSenha(usuarioTeste.idUsuario, request)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `deve logar com sucesso`() {
        val loginRequest = UsuarioLoginRequest(email = "teste@email.com", senha = "senha123")
        `when`(usuarioService.autenticarUsuario(loginRequest)).thenReturn(usuarioTeste)

        val response = controller.logar(loginRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    @Test
    fun `deve deslogar com sucesso`() {
        val loginRequest = UsuarioLoginRequest(email = "teste@email.com", senha = "senha123")
        `when`(usuarioService.desautenticarUsuario(loginRequest)).thenReturn(usuarioTeste)

        val response = controller.deslogar(loginRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    @Test
    fun `deve desativar usuario com sucesso`() {
        `when`(usuarioService.desativarUsuario(usuarioTeste.idUsuario)).thenReturn(usuarioTeste)

        val response = controller.desativarUsuario(usuarioTeste.idUsuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    @Test
    fun `deve ativar usuario com sucesso`() {
        `when`(usuarioService.ativarUsuario(usuarioTeste.idUsuario)).thenReturn(usuarioTeste)

        val response = controller.ativarUsuario(usuarioTeste.idUsuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }
}
