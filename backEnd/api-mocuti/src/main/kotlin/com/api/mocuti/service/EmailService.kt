package com.api.mocuti.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import com.api.mocuti.entity.Evento
import org.springframework.mail.javamail.MimeMessageHelper

@Service
class EmailService(
    // é a injeção automática do cliente de envio de e-mails que o Spring Boot cria com base nas configurações do application.properties
    private val mailSender: JavaMailSender //interface do Spring
) {

    @Async // executa em outra thread para não travar a requisição
    fun enviarEmailNovoEvento(destinatario: String, nome: String, evento: Evento) {
        val mensagem = MimeMessageHelper(mailSender.createMimeMessage(), true)
        mensagem.setFrom("kevelly.oliveira@sptech.school") // precisa ser o mesmo do SMTP
        mensagem.setTo(destinatario)
        mensagem.setSubject("Novo evento: ${evento.nomeEvento}")
        val html = """
            <html>
            Olá, $nome! 

            Um novo evento foi criado na sua categoria favorita: ${evento.categoria.nome}

            Evento: ${evento.nomeEvento}
            Descrição: ${evento.descricao}
             <html>body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #fff; padding: 20px; border-radius: 8px;">
                    <h2 style="color: #333;">Novo Evento Criado!</h2>
                    <p>Olá, <strong>$nome</strong>!</p>
                    <p>Um novo evento foi criado na sua categoria favorita: <strong>${evento.categoria.nome}</strong></p>
                    <h3 style="color: #555;">Detalhes do Evento:</h3>
                    <ul>
                        <li><strong>Nome do Evento:</strong> ${evento.nomeEvento}</li>
                        <li><strong>Descrição:</strong> ${evento.descricao}</li>
                    </ul>
                    <p style="font-size: 12px; color: #777; margin-top: 20px;">
                        Você está recebendo este e-mail porque se inscreveu para receber notificações sobre novos eventos.
                    </p>
                </div>
            """.trimIndent()

        mensagem.setText(html, true)
        mailSender.send(mensagem.mimeMessage)
    }

    @Async
    fun enviarEmailResetSenha(destinatario: String, token: String) {
        val tokenFormatado = token.padStart(6, '0')

        val html = """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
            <div style="max-width: 500px; margin: auto; background-color: #fff; padding: 20px; border-radius: 8px; text-align: center;">
                <h2 style="color: #333;">Token de autenticação</h2>
                <p>Enviado para <strong>$destinatario</strong></p>
                <p>Insira o token abaixo para finalizar:</p>
                <div style="margin: 20px 0; display: flex; flex-direction: column; justify-content: center; gap: 10px;">
                    ${
            tokenFormatado.map { "<span style='display: inline-block; width: 40px; height: 50px; line-height: 50px; border: 2px solid #6b1b4d; border-radius: 5px; font-size: 24px; font-weight: bold;'>$it</span>" }
                .joinToString("")
        }
                </div>
                <p style="font-size: 12px; color: #777; margin-top: 20px;">
                    Se não foi você, ignore este e-mail.
                </p>
            </div>
        </body>
        </html>
    """.trimIndent()

        val message = MimeMessageHelper(mailSender.createMimeMessage(), true)
        message.setFrom("kevelly.oliveira@sptech.school")
        message.setTo(destinatario)
        message.setSubject("Recuperação de Senha")
        message.setText(html, true) // true indica que é HTML

        mailSender.send(message.mimeMessage)
    }

}
