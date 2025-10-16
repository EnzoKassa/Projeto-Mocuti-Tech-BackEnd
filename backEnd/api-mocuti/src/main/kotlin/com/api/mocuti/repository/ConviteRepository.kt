package com.api.mocuti.repository
import com.api.mocuti.entity.Convite
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface ConviteRepository : JpaRepository<Convite, Int> {
    fun existsByEventoAndUsuarioConvidado(evento: Evento, usuarioConvidado: Usuario): Boolean
    fun findByUsuarioConvidado_IdUsuario(idUsuario: Int): List<Convite>
    fun findByEvento_IdEvento(idEvento: Int): List<Convite>

}