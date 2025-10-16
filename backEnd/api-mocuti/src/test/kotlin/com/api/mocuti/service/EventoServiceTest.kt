package com.api.mocuti.service

import com.api.mocuti.dto.EventoAttDiaHoraRequest
import com.api.mocuti.dto.EventoAtualizaStatusRequest
import com.api.mocuti.dto.EventoAtualizarRequest
import com.api.mocuti.dto.EventoCadastroRequest
import com.api.mocuti.entity.Categoria
import com.api.mocuti.entity.Endereco
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.StatusEvento
import com.api.mocuti.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class EventoServiceTest {

    private val eventoRepository: EventoRepository = mock(EventoRepository::class.java)
    private val enderecoRepository: EnderecoRepository = mock(EnderecoRepository::class.java)
    private val statusEventoRepository: StatusEventoRepository = mock(StatusEventoRepository::class.java)
    private val categoriaRepository: CategoriaRepository = mock(CategoriaRepository::class.java)
    private val preferenciaRepository: PreferenciaRepository = mock(PreferenciaRepository::class.java)
    private val emailService: EmailService = mock(EmailService::class.java)

    private lateinit var service: EventoService

    private lateinit var endereco: Endereco
    private lateinit var statusEvento: StatusEvento
    private lateinit var categoria: Categoria
    private lateinit var evento: Evento

    @BeforeEach
    fun setup() {
        service = EventoService(
            eventoRepository,
            enderecoRepository,
            statusEventoRepository,
            categoriaRepository,
            preferenciaRepository,
            emailService
        )

        endereco = Endereco(1, "12345678", "Rua A", 100, "Ap 2", "SP", "São Paulo", "Centro")
        statusEvento = StatusEvento(1, "Ativo")
        categoria = Categoria(1, "Oficina", "Oficinas de aprendizado")

        evento = Evento(
            idEvento = 1,
            nomeEvento = "Evento Teste",
            descricao = "Descrição",
            dia = LocalDate.now(),
            horaInicio = LocalTime.of(10, 0),
            horaFim = LocalTime.of(12, 0),
            isAberto = true,
            qtdVaga = 10,
            publicoAlvo = "Geral",
            qtdInteressado = 5,
            foto = ByteArray(0),
            endereco = endereco,
            statusEvento = statusEvento,
            categoria = categoria
        )
    }

    @Test
    fun `deve criar evento com sucesso`() {
        val dto = EventoCadastroRequest(
            nomeEvento = "Novo Evento",
            descricao = "Descrição",
            dia = LocalDate.now(),
            horaInicio = LocalTime.of(10, 0),
            horaFim = LocalTime.of(12, 0),
            isAberto = true,
            qtdVaga = 20,
            publicoAlvo = "Estudantes",
            qtdInteressado = 0,
            enderecoId = 1,
            statusEventoId = 1,
            categoriaId = 1
        )

        `when`(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco))
        `when`(statusEventoRepository.findById(1)).thenReturn(Optional.of(statusEvento))
        `when`(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria))
        `when`(preferenciaRepository.findUsuariosByIdCategoria(1)).thenReturn(emptyList())
        `when`(eventoRepository.save(ArgumentMatchers.any(Evento::class.java))).thenAnswer { invocation ->
            val eventoSalvo = invocation.arguments[0] as Evento
            eventoSalvo.copy(idEvento = 1)
        }

        val result = service.criarEvento(dto)

        assertEquals("Novo Evento", result.nomeEvento)
        assertEquals(1, result.idEvento)
        verify(eventoRepository).save(ArgumentMatchers.any(Evento::class.java))
    }


    @Test
    fun `deve atualizar evento com sucesso`() {
        val dto = EventoAtualizarRequest(
            nomeEvento = "Atualizado",
            descricao = "Nova descrição",
            dia = LocalDate.now(),
            horaInicio = LocalTime.of(9, 0),
            horaFim = LocalTime.of(11, 0),
            isAberto = false,
            qtdVaga = 15,
            publicoAlvo = "Professores",
            qtdInteressado = 3,
            enderecoId = 1,
            statusEventoId = 1,
            categoriaId = 1
        )

        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco))
        `when`(statusEventoRepository.findById(1)).thenReturn(Optional.of(statusEvento))
        `when`(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria))
        `when`(eventoRepository.save(any(Evento::class.java))).thenReturn(evento)

        val result = service.atualizarEvento(1, dto)

        assertEquals("Atualizado", result.nomeEvento)
        assertFalse(result.isAberto)
        verify(eventoRepository).save(evento)
    }

    @Test
    fun `deve atualizar dia e hora do evento`() {
        val dto = EventoAttDiaHoraRequest(
            dia = LocalDate.of(2025, 12, 25),
            horaInicio = LocalTime.of(14, 0),
            horaFim = LocalTime.of(16, 0)
        )

        `when`(eventoRepository.existsById(1)).thenReturn(true)
        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))

        val result = service.atualizarDiaHora(1, dto)

        verify(eventoRepository).atualizarDiaHora(1, dto.dia, dto.horaInicio, dto.horaFim)
        assertEquals(evento, result)
    }

    @Test
    fun `deve atualizar status do evento`() {
        val novoStatus = StatusEvento(2, "Finalizado")
        val dto = EventoAtualizaStatusRequest(idStatusEvento = 2)

        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(statusEventoRepository.findById(2)).thenReturn(Optional.of(novoStatus))
        `when`(eventoRepository.save(any(Evento::class.java))).thenReturn(evento)

        val result = service.atualizarStatusEvento(1, dto)

        assertEquals(2, result.statusEvento.idStatusEvento)
        verify(eventoRepository).save(evento)
    }

    @Test
    fun `deve lançar excecao quando evento nao encontrado para atualizar status`() {
        val dto = EventoAtualizaStatusRequest(idStatusEvento = 2)

        `when`(eventoRepository.findById(99)).thenReturn(Optional.empty())

        val excecao = assertThrows(NoSuchElementException::class.java) {
            service.atualizarStatusEvento(99, dto)
        }

        assertEquals("Evento com ID 99 não encontrado", excecao.message)
    }
}
