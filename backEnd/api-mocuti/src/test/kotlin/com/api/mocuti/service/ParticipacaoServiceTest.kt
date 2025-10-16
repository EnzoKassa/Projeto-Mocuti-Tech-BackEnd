package com.api.mocuti.service

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.entity.*
import com.api.mocuti.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDate
import java.util.*

class ParticipacaoServiceTest {

    private lateinit var participacaoRepository: ParticipacaoRepository
    private lateinit var feedbackRepository: FeedbackRepository
    private lateinit var eventoRepository: EventoRepository
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var statusInscricaoRepository: StatusInscricaoRepository
    private lateinit var service: ParticipacaoService

    private lateinit var usuario: Usuario
    private lateinit var evento: Evento
    private lateinit var statusInscricao: StatusInscricao

    @BeforeEach
    fun setUp() {
        participacaoRepository = mock(ParticipacaoRepository::class.java)
        feedbackRepository = mock(FeedbackRepository::class.java)
        eventoRepository = mock(EventoRepository::class.java)
        usuarioRepository = mock(UsuarioRepository::class.java)
        statusInscricaoRepository = mock(StatusInscricaoRepository::class.java)
        service = ParticipacaoService(
            participacaoRepository,
            feedbackRepository,
            eventoRepository,
            usuarioRepository,
            statusInscricaoRepository
        )

        val cargo = Cargo(1, "Cargo Teste")
        val endereco = Endereco(1, "Rua X", "123", 5, "Cidade", "Estado", "SP", "Bairro")
        val canalComunicacao = CanalComunicacao(1, "Email")

        usuario = Usuario(
            idUsuario = 1,
            nomeCompleto = "Usuário Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "email@test.com",
            dt_nasc = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileiro",
            genero = "Masculino",
            senha = "123456",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            cargo = cargo,
            endereco = endereco,
            canalComunicacao = canalComunicacao
        )

        val enderecoEvento = Endereco(2, "Rua Y", "456", 10, "Cidade", "Estado", "SP", "Bairro")
        val statusEvento = StatusEvento(1, "Ativo")
        val categoria = Categoria(1, "Categoria Teste", "Descrição")

        evento = Evento(
            idEvento = 1,
            nomeEvento = "Evento Teste",
            descricao = "Descrição do Evento",
            dia = LocalDate.now(),
            horaInicio = java.time.LocalTime.of(9, 0), // agora é não-nulo
            horaFim = java.time.LocalTime.of(17, 0),  // agora é não-nulo
            isAberto = true,
            qtdVaga = 100,
            qtdInteressado = 50,
            publicoAlvo = "Estudantes",
            foto = null,
            endereco = enderecoEvento,
            statusEvento = statusEvento,
            categoria = categoria
        )

        statusInscricao = StatusInscricao(1, "Confirmado")
    }

    @Test
    fun `inscreverUsuario deve salvar participacao quando nao existe`() {
        `when`(participacaoRepository.findById(ParticipacaoId(1, 1))).thenReturn(Optional.empty())
        `when`(eventoRepository.findById(1)).thenReturn(Optional.of(evento))
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(statusInscricaoRepository.findById(1)).thenReturn(Optional.of(statusInscricao))

        service.inscreverUsuario(1, 1, 1)

        verify(participacaoRepository).save(any(Participacao::class.java))
    }

    @Test
    fun `inscreverUsuario deve lancar excecao se participacao ja existe`() {
        val participacaoExistente = Participacao(ParticipacaoId(1, 1), true, false, statusInscricao, usuario, evento)
        `when`(participacaoRepository.findById(ParticipacaoId(1, 1))).thenReturn(Optional.of(participacaoExistente))

        val ex = assertThrows<IllegalStateException> {
            service.inscreverUsuario(1, 1, 1)
        }
        assertEquals("Usuário já está inscrito neste evento.", ex.message)
    }

    @Test
    fun `cancelarInscricao deve deletar participacao existente`() {
        val participacao = Participacao(ParticipacaoId(1, 1), true, false, statusInscricao, usuario, evento)
        `when`(participacaoRepository.findById(ParticipacaoId(1, 1))).thenReturn(Optional.of(participacao))

        service.cancelarInscricao(1, 1)

        verify(participacaoRepository).delete(participacao)
    }

    @Test
    fun `cancelarInscricao deve lancar excecao se participacao nao existe`() {
        `when`(participacaoRepository.findById(ParticipacaoId(1, 1))).thenReturn(Optional.empty())

        val ex = assertThrows<NoSuchElementException> {
            service.cancelarInscricao(1, 1)
        }
        assertEquals("Participação não encontrada para o evento 1 e usuário 1", ex.message)
    }

    @Test
    fun `listarEventosInscritos deve retornar eventos do usuario`() {
        val participacoes = listOf(Participacao(ParticipacaoId(1, 1), true, false, statusInscricao, usuario, evento))
        `when`(participacaoRepository.findByUsuario_IdUsuarioAndIsInscritoTrue(1)).thenReturn(participacoes)

        val result = service.listarEventosInscritos(1)

        assertEquals(1, result.size)
        assertEquals("Evento Teste", result[0].nomeEvento)
    }
}
