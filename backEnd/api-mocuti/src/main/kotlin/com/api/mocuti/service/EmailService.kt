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
        mensagem.setSubject("Mocuti - Novo Evento na sua Categoria Favorita!")
        val html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8" />
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;">
              <div style="max-width: 600px; margin: auto; background-color: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden;">
                
                <!-- Faixa colorida no topo -->
                <div style="height: 8px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
            
                <div style="padding: 25px;">
                  <h2 style="color: #000; text-align: center; margin-bottom: 20px;">🎉 Novo Evento Criado!</h2>
            
                  <p style="font-size: 16px; color: #333;">Olá, <strong style="color:#008000;">$nome</strong>! 👋</p>
                  <p style="font-size: 15px; color: #333;">
                    Um novo evento foi criado na sua categoria favorita: 
                    <strong style="color: #3c9cea;">${evento.categoria.nome}</strong>
                  </p>
            
                  <div style="background: #f9f9f9; border-left: 6px solid #d4c300; padding: 15px 20px; border-radius: 6px; margin-top: 15px;">
                    <h3 style="color: #b30000; margin-top: 0;">📌 Detalhes do Evento</h3>
                    <p style="margin: 6px 0;"><strong style="color:#000;">Nome:</strong> ${evento.nomeEvento}</p>
                    <p style="margin: 6px 0;"><strong style="color:#000;">Descrição:</strong> ${evento.descricao}</p>
                  </div>
            
                  <hr style="margin: 30px 0; border: none; border-top: 1px solid #ddd;" />
            
                  <p style="font-size: 12px; color: #777; text-align: center;">
                    Você está recebendo este e-mail porque se inscreveu para receber notificações sobre novos eventos.
                  </p>
                </div>
            
                <!-- Faixa colorida no rodapé -->
                <div style="height: 6px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
              </div>
            </body>
            </html>
            """.trimIndent()

        mensagem.setText(html, true)
        mailSender.send(mensagem.mimeMessage)
    }

    @Async
    fun enviarEmailResetSenha(destinatario: String, token: String) {
        val tokenFormatado = token.padStart(6, '0')

        val html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8" />
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;">
              <div style="max-width: 500px; margin: auto; background-color: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden;">
              
                <!-- Faixa colorida no topo -->
                <div style="height: 8px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
              
                <div style="padding: 25px; text-align: center;">
                  <h2 style="color:#000; margin-bottom: 10px;">🔐 Token de Autenticação</h2>
                  <p style="color: #333;">Enviado para <strong style="color: rgba(61, 165, 225, 1);">$destinatario</strong></p>
                  <p style="margin-top: 10px; color: #333;">Insira o token abaixo para finalizar:</p>
              
                  <!-- container dos números -->
                  <div style="margin: 25px 0; text-align: center;">
                    ${
                        tokenFormatado.map {
                            "<span style='display: inline-block; width: 45px; height: 55px; margin: 0 6px; background-color: #fff; border: 3px solid rgba(61, 165, 225, 1); color: #000; border-radius: 8px; font-size: 22px; font-weight: bold; line-height: 55px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>$it</span>"
                        }.joinToString("")
                    }
                  </div>
              
                  <p style="font-size: 12px; color: #777; margin-top: 25px;">
                    Se não foi você, ignore este e-mail.
                  </p>
                </div>
              
                <!-- Faixa colorida no rodapé -->
                <div style="height: 6px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
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

    @Async
    fun enviarEmailConviteEvento(destinatario: String, nome: String, evento: Evento) {
        val mensagem = MimeMessageHelper(mailSender.createMimeMessage(), true)
        mensagem.setFrom("kevelly.oliveira@sptech.school")
        mensagem.setTo(destinatario)
        mensagem.setSubject("Mocuti - Você foi Convidado para um Evento!")
        val html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8" />
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;">
              <div style="max-width: 600px; margin: auto; background-color: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden;">
                
                <div style="height: 8px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
            
                <div style="padding: 25px;">
                  <h2 style="color: #000; text-align: center; margin-bottom: 20px;">✉️ Você tem um Novo Convite!</h2>
            
                  <p style="font-size: 16px; color: #333;">Olá, <strong style="color:#008000;">$nome</strong>! 👋</p>
                  <p style="font-size: 15px; color: #333;">
                    Você foi convidado(a) para participar do evento: 
                    <strong style="color: #3c9cea;">${evento.nomeEvento}</strong>
                  </p>
                  <p style="font-size: 15px; color: #333;">
                    Fique atento(a) aos detalhes abaixo e confirme sua participação!
                  </p>
            
                  <div style="background: #f9f9f9; border-left: 6px solid #d4c300; padding: 15px 20px; border-radius: 6px; margin-top: 15px;">
                    <h3 style="color: #b30000; margin-top: 0;">📌 Detalhes do Evento</h3>
                    <p style="margin: 6px 0;"><strong style="color:#000;">Nome:</strong> ${evento.nomeEvento}</p>
                    <p style="margin: 6px 0;"><strong style="color:#000;">Descrição:</strong> ${evento.descricao}</p>
                    <p style="margin: 6px 0;"><strong style="color:#000;">Data:</strong> ${evento.dia}</p>
                  </div>
            
                  <hr style="margin: 30px 0; border: none; border-top: 1px solid #ddd;" />
            
                  <p style="font-size: 12px; color: #777; text-align: center;">
                    Você está recebendo este e-mail porque foi convidado para um evento através da plataforma Mocuti.
                  </p>
                </div>
            
                <div style="height: 6px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
              </div>
            </body>
            </html>
        """.trimIndent()
        mensagem.setText(html, true)
        mailSender.send(mensagem.mimeMessage)
    }


    @Async
    fun enviarEmailStatusParticipacao(destinatario: String, nome: String, status: String, evento: Evento) {

        val mensagem = MimeMessageHelper(mailSender.createMimeMessage(), true)
        mensagem.setFrom("kevelly.oliveira@sptech.school")
        mensagem.setTo("kevellyoliveira2@gmail.com")
        mensagem.setSubject("Atualização do seu status no evento – ${evento.nomeEvento}")

        val html = """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
          <meta charset="UTF-8" />
        </head>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;">
          <div style="max-width: 600px; margin: auto; background-color: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden;">

            <div style="height: 8px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>

            <div style="padding: 25px;">
              <h2 style="color: #000; text-align: center; margin-bottom: 20px;">📩 Status Atualizado!</h2>

              <p style="font-size: 16px; color: #333;">Olá👋</p>
              <p style="font-size: 15px; color: #333;"><strong style="color:#008000;">$nome</strong> atualizou o status no evento <strong style="color:#3c9cea;">${evento.nomeEvento}</strong> para:</p>

              <div style="margin: 25px auto; width: fit-content; font-size: 22px; font-weight: bold; padding: 12px 22px; border-radius: 10px; background-color: #f9f9f9; border-left: 6px solid #3c9cea;">
                $status
              </div>

              <p style="font-size: 14px; color:#555; margin-top: 20px;">
                Caso tenha sido um engano, você pode alterar seu status novamente no sistema.
              </p>

              <hr style="margin: 30px 0; border: none; border-top: 1px solid #ddd;" />

              <p style="font-size: 12px; color: #777; text-align: center;">
                Você recebeu este e-mail porque está inscrito no evento.
              </p>
            </div>

            <div style="height: 6px; background: linear-gradient(90deg, rgba(69, 170, 72, 1) 0%, rgba(61, 165, 225, 1) 35%, rgba(239, 231, 57, 1) 68%, rgba(255, 72, 72, 1) 100%);"></div>
          </div>
        </body>
        </html>
    """.trimIndent()

        mensagem.setText(html, true)
        mailSender.send(mensagem.mimeMessage)
    }

}
