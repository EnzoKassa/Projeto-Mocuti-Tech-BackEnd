package com.api.mocuti.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import com.api.mocuti.entity.Evento

@Service
class EmailService(
    // é a injeção automática do cliente de envio de e-mails que o Spring Boot cria com base nas configurações do application.properties
    private val mailSender: JavaMailSender //interface do Spring
) {

    @Async // executa em outra thread para não travar a requisição
    fun enviarEmailNovoEvento(destinatario: String, nome: String, evento: Evento) {
        val mensagem = SimpleMailMessage()
        mensagem.setFrom("kevelly.oliveira@sptech.school") // precisa ser o mesmo do SMTP
        mensagem.setTo(destinatario)
        mensagem.setSubject("Novo evento: ${evento.nomeEvento}")
        mensagem.setText(
            """
            Olá, $nome! 

            Um novo evento foi criado na sua categoria favorita: ${evento.categoria.nome}

            Evento: ${evento.nomeEvento}
            Descrição: ${evento.descricao}
            """.trimIndent()
        )
        mailSender.send(mensagem)
    }
}
