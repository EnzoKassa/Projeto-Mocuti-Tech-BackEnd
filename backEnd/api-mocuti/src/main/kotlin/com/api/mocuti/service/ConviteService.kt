package com.api.mocuti.service

import com.api.mocuti.entity.Convite
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.StatusConvite
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.ConviteRepository
import com.api.mocuti.repository.EventoRepository
import com.api.mocuti.repository.UsuarioRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ConviteService(
    private val conviteRepository: ConviteRepository,
    private val usuarioRepository: UsuarioRepository,
    private val eventoRepository: EventoRepository
) {

    fun enviarConvite(idEvento: Int, idRemetente: Int, idsConvidados: List<Int>) {
        val evento = eventoRepository.findById(idEvento)
            .orElseThrow { NoSuchElementException("Evento não encontrado") }

        val remetente = usuarioRepository.findById(idRemetente)
            .orElseThrow { NoSuchElementException("Remetente não encontrado") }

        idsConvidados.forEach { idConvidado ->
            val convidado = usuarioRepository.findById(idConvidado)
                .orElseThrow { NoSuchElementException("Convidado com ID $idConvidado não encontrado") }

            // Verifique se o convite já existe para evitar duplicação
            if (!conviteRepository.existsByEventoAndUsuarioConvidado(evento, convidado)) {
                val convite = Convite(
                    evento = evento,
                    usuarioConvidado = convidado,
                    usuarioRemetente = remetente,
                    statusConvite = StatusConvite.EM_ANDAMENTO,
                    dataConvite = LocalDate.now()
                )
                conviteRepository.save(convite)
            }
        }
    }

    fun aceitarConvite(idConvite: Int) {
        val convite = conviteRepository.findById(idConvite)
            .orElseThrow { NoSuchElementException("Convite não encontrado") }

        convite.statusConvite = StatusConvite.ACEITO
        conviteRepository.save(convite)
    }

    fun cancelarConvite(idConvite: Int) {
        val convite = conviteRepository.findById(idConvite)
            .orElseThrow { NoSuchElementException("Convite não encontrado") }

        convite.statusConvite = StatusConvite.CANCELADO
        conviteRepository.save(convite)
    }

    fun listarConvitesPorUsuario(idUsuario: Int): List<Convite> {
        return conviteRepository.findByUsuarioConvidado_IdUsuario(idUsuario)
    }

    fun listarConvitesPorEvento(idEvento: Int): List<Convite> {
        return conviteRepository.findByEvento_IdEvento(idEvento)
    }


}