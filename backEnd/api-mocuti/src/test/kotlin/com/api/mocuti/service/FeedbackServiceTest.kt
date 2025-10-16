package com.api.mocuti.service

import com.api.mocuti.dto.FeedbackNovoRequest
import com.api.mocuti.entity.*
import com.api.mocuti.repository.*
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

    private val endereco = Endereco(1, "12345-678", "Rua Teste", 100, "Ap 2", "SP", "Cidade Teste", "Bairro Teste")
    private val statusEvento = StatusEvento(1, "Ativo")
    private val categoria = Categoria(1, "Categoria Teste", "Descrição Categoria")
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
    private val usuario = Usuario(
        idUsuario = 1,
        nomeCompleto = "Usuário Teste",
        cpf = "123.456.789-00",
        telefone = "(11)91234-5678",
        email = "email@test.com",
        dt_nasc = LocalDate.of(1990, 1, 1),
        etnia = "Pardo",
        nacionalidade = "Brasileiro",
        genero = "Masculino",
        senha = "senha123",
        cargo = Cargo(1, "Analista"),
        endereco = endereco,
        canalComunicacao = CanalComunicacao(1, "Email")
    )
    private val nota = NotaFeedback(1, "Nota Teste")

    @BeforeEach
    fun setUp() {
        feedbackRepository = mock(FeedbackRepository::class.java)
        notaRepository = mock(NotaFeedbackRepository::class.java)
        eventoRepository = mock(EventoRepository::class.java)
        usuarioRepository = mock(UsuarioRepository::class.java)
        service = FeedbackService(feedbackRepository, notaRepository, eventoRepository, usuarioRepository)
    }

    @Test
    fun `criar novo feedback quando não existe`() {
        val request = FeedbackNovoRequest("Muito bom", 1, 1, null)

        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(feedbackRepository.findByUsuarioAndEvento(usuario, evento)).thenReturn(null)
        `when`(feedbackRepository.save(any(Feedback::class.java))).thenAnswer { it.arguments[0] }

        val result = service.criarOuAtualizar(request)

        assertEquals("Muito bom", result.comentario)
        assertEquals(evento, result.evento)
        assertEquals(usuario, result.usuario)
        assertNull(result.nota)
        verify(feedbackRepository).save(any(Feedback::class.java))
    }

    @Test
    fun `atualizar feedback existente`() {
        val feedbackExistente = Feedback(1, "Comentário antigo", LocalDate.now(), null, evento, usuario)
        val request = FeedbackNovoRequest("Comentário novo", 1, 1, 1)

        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(feedbackRepository.findByUsuarioAndEvento(usuario, evento)).thenReturn(feedbackExistente)
        `when`(notaRepository.findById(1)).thenReturn(Optional.of(nota))
        `when`(feedbackRepository.save(any(Feedback::class.java))).thenAnswer { it.arguments[0] }

        val result = service.criarOuAtualizar(request)

        assertEquals("Comentário novo", result.comentario)
        assertEquals(nota, result.nota)
        verify(feedbackRepository).save(feedbackExistente)
    }

    @Test
    fun `lança exceção se evento não encontrado`() {
        val request = FeedbackNovoRequest("Comentário", 99, 1, null)
        `when`(eventoRepository.findById(99)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.criarOuAtualizar(request)
        }
        assertEquals("Evento não encontrado", ex.message)
        verify(feedbackRepository, never()).save(any())
    }

    @Test
    fun `lança exceção se usuário não encontrado`() {
        val request = FeedbackNovoRequest("Comentário", 1, 99, null)
        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(99)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.criarOuAtualizar(request)
        }
        assertEquals("Usuário não encontrado", ex.message)
        verify(feedbackRepository, never()).save(any())
    }

    @Test
    fun `lança exceção se nota inválida`() {
        val feedbackExistente = Feedback(1, "Comentário antigo", LocalDate.now(), null, evento, usuario)
        val request = FeedbackNovoRequest("Comentário novo", 1, 1, 99)

        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(feedbackRepository.findByUsuarioAndEvento(usuario, evento)).thenReturn(feedbackExistente)
        `when`(notaRepository.findById(99)).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            service.criarOuAtualizar(request)
        }
        assertEquals("Nota inválida", ex.message)
        verify(feedbackRepository, never()).save(any())
    }
}
