package com.api.mocuti.service

import com.api.mocuti.entity.Categoria
import com.api.mocuti.entity.Evento
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class EmailServiceTest {

    private lateinit var mailSender: JavaMailSender
    private lateinit var emailService: EmailService

    private lateinit var evento: Evento

    @BeforeEach
    fun setUp() {
        mailSender = mock(JavaMailSender::class.java)
        emailService = EmailService(mailSender)

        val categoria = Categoria(1, "Categoria Teste", "Descrição Categoria")
        evento = Evento(
            idEvento = 1,
            nomeEvento = "Evento Teste",
            descricao = "Descrição do evento",
            dia = java.time.LocalDate.now(),
            horaInicio = java.time.LocalTime.of(9,0),
            horaFim = java.time.LocalTime.of(17,0),
            isAberto = true,
            qtdVaga = 100,
            qtdInteressado = 50,
            publicoAlvo = "Estudantes",
            foto = null,
            endereco = mock(com.api.mocuti.entity.Endereco::class.java),
            statusEvento = mock(com.api.mocuti.entity.StatusEvento::class.java),
            categoria = categoria
        )
    }

    @Test
    fun `enviarEmailNovoEvento deve enviar SimpleMailMessage`() {
        val captor = ArgumentCaptor.forClass(SimpleMailMessage::class.java)
        val destinatario = "teste@teste.com"
        val nome = "Usuário"

        emailService.enviarEmailNovoEvento(destinatario, nome, evento)

        verify(mailSender).send(captor.capture())

        val mensagemEnviada = captor.value
        // verifica destinatário
        val destinatarios = mensagemEnviada.to ?: arrayOf()
        assertEquals(1, destinatarios.size)
        assertEquals(destinatario, destinatarios[0])

        // verifica conteúdo do texto
        val texto = mensagemEnviada.text ?: ""
        assertTrue(texto.contains("Evento: ${evento.nomeEvento}"))
        assertTrue(texto.contains("Um novo evento foi criado na sua categoria favorita"))
    }

    @Test
    fun `enviarEmailResetSenha deve enviar MimeMessage`() {
        val mimeMessage = mock(MimeMessage::class.java)
        `when`(mailSender.createMimeMessage()).thenReturn(mimeMessage)

        val token = "123456"
        val destinatario = "teste@teste.com"

        emailService.enviarEmailResetSenha(destinatario, token)

        verify(mailSender).send(mimeMessage)
    }
}
