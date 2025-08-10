package com.api.mocuti.service

import com.api.mocuti.dto.FeedbackAtualizarRequest
import com.api.mocuti.dto.FeedbackNovoRequest
import com.api.mocuti.entity.*
import com.api.mocuti.repository.EventoRepository
import com.api.mocuti.repository.FeedbackRepository
import com.api.mocuti.repository.NotaFeedbackRepository
import com.api.mocuti.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class FeedbackServiceTest {

    private lateinit var feedbackRepository: FeedbackRepository
    private lateinit var notaRepository: NotaFeedbackRepository
    private lateinit var eventoRepository: EventoRepository
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var service: FeedbackService

    // Objetos auxiliares para compor Evento e Usuario válidos
    private val endereco = Endereco(
        idEndereco = 1,
        cep = "12345-678",
        logradouro = "Rua Teste",
        numero = 100,
        complemento = "Complemento",
        uf = "SP",
        estado = "Bairro Teste",
        bairro = "Cidade Teste",
    )
    private val statusEvento = StatusEvento(
        idStatusEvento = 1,
        situacao = "Ativo"
    )

    private val categoria = Categoria(
        idCategoria = 1,
        nome = "Categoria Teste",
        descricao = "Categoria Teste"
    )

    private val cargo = Cargo(
        idCargo = 1,
        tipoCargo = "Analista"
    )

    private val canalComunicacao = CanalComunicacao(
        idCanalComunicacao = 1,
        tipoCanalComunicacao = "Email"
    )

    // Entidade Evento com todos os campos obrigatórios preenchidos
    private val evento = Evento(
        idEvento = 1,
        nomeEvento = "Evento Teste",
        descricao = "Descrição do evento",
        dia = LocalDate.now(),
        horaInicio = LocalTime.of(9, 0),
        horaFim = LocalTime.of(17, 0),
        isAberto = true,
        qtdVaga = 100,
        qtdInteressado = 50,
        publicoAlvo = "Estudantes",
        foto = null,
        endereco = endereco,
        statusEvento = statusEvento,
        categoria = categoria
    )

    // Entidade Usuario com todos os campos obrigatórios preenchidos
    private val usuario = Usuario(
        idUsuario = 1,
        nomeCompleto = "Usuário Teste",
        cpf = "123.456.789-00",
        telefone = "(11) 91234-5678",
        email = "email@test.com",
        dt_nasc = LocalDate.of(1990, 1, 1),
        etnia = "Pardo",
        nacionalidade = "Brasileiro",
        genero = "Masculino",
        senha = "senha123",
        cargo = cargo,
        endereco = endereco,
        canalComunicacao = canalComunicacao
    )

    private val nota = NotaFeedback(
        idNotaFeedback = 1,
        tipoNota = "Nota Teste"
    )

    @BeforeEach
    fun setUp() {
        feedbackRepository = mock(FeedbackRepository::class.java)
        notaRepository = mock(NotaFeedbackRepository::class.java)
        eventoRepository = mock(EventoRepository::class.java)
        usuarioRepository = mock(UsuarioRepository::class.java)
        service = FeedbackService(feedbackRepository, notaRepository, eventoRepository, usuarioRepository)
    }

    @Test
    fun `criar deve salvar feedback com sucesso`() {
        val request = FeedbackNovoRequest(
            comentario = "Muito bom",
            idEvento = 1,
            idUsuario = 1
        )

        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        val feedbackSalvo = Feedback(1, "Muito bom", LocalDate.now(), null, evento, usuario)
        `when`(feedbackRepository.save(any(Feedback::class.java))).thenReturn(feedbackSalvo)

        val result = service.criar(request)

        assertEquals("Muito bom", result.comentario)
        assertEquals(evento, result.evento)
        assertEquals(usuario, result.usuario)
        verify(feedbackRepository).save(any(Feedback::class.java))
    }

    @Test
    fun `criar deve lançar exceção se evento não encontrado`() {
        val request = FeedbackNovoRequest("Comentário", 1, 1)
        `when`(eventoRepository.findById(1)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.criar(request)
        }
        assertEquals("Evento não encontrado", ex.message)
        verify(feedbackRepository, never()).save(any())
    }

    @Test
    fun `criar deve lançar exceção se usuário não encontrado`() {
        val request = FeedbackNovoRequest("Comentário", 1, 1)
        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.criar(request)
        }
        assertEquals("Usuário não encontrado", ex.message)
        verify(feedbackRepository, never()).save(any())
    }

    @Test
    fun `atualizar deve alterar comentario e nota`() {
        val feedbackExistente = Feedback(1, "Antigo", LocalDate.now(),null, evento, usuario)
        val dto = FeedbackAtualizarRequest(comentario = "Novo comentário", idNota = 1)

        `when`(feedbackRepository.findById(1)).thenReturn(Optional.of(feedbackExistente))
        `when`(notaRepository.findById(1)).thenReturn(Optional.of(nota))
        val feedbackSalvo = Feedback(1, "Novo comentário", LocalDate.now(),  nota, evento, usuario)
        `when`(feedbackRepository.save(any(Feedback::class.java))).thenReturn(feedbackSalvo)

        val result = service.atualizar(1, dto)

        assertEquals("Novo comentário", result.comentario)
        assertEquals(nota, result.nota)
        verify(feedbackRepository).save(any(Feedback::class.java))
    }

    @Test
    fun `atualizar deve alterar apenas comentario se idNota for null`() {
        val feedbackExistente = Feedback(1, "Antigo", LocalDate.now(), null, evento, usuario)
        val dto = FeedbackAtualizarRequest(comentario = "Apenas comentário", idNota = null)

        `when`(feedbackRepository.findById(1)).thenReturn(Optional.of(feedbackExistente))
        `when`(feedbackRepository.save(any(Feedback::class.java))).thenAnswer { it.arguments[0] }

        val result = service.atualizar(1, dto)

        assertEquals("Apenas comentário", result.comentario)
        assertNull(result.nota)
        verify(notaRepository, never()).findById(anyInt())
        verify(feedbackRepository).save(any(Feedback::class.java))
    }

    @Test
    fun `atualizar deve lançar exceção se feedback não encontrado`() {
        val dto = FeedbackAtualizarRequest("Comentário", 1)
        `when`(feedbackRepository.findById(1)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.atualizar(1, dto)
        }
        assertEquals("Feedback não encontrado", ex.message)
        verify(feedbackRepository, never()).save(any())
    }

    @Test
    fun `atualizar deve lançar exceção se nota não encontrada`() {
        val feedbackExistente = Feedback(1, "Antigo", LocalDate.now(), null, evento, usuario)
        val dto = FeedbackAtualizarRequest("Comentário", 1)

        `when`(feedbackRepository.findById(1)).thenReturn(Optional.of(feedbackExistente))
        `when`(notaRepository.findById(1)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.atualizar(1, dto)
        }
        assertEquals("Nota não encontrada", ex.message)
        verify(feedbackRepository, never()).save(any())
    }
}
