package com.api.mocuti.repository

import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Participacao
import com.api.mocuti.entity.ParticipacaoId
import com.api.mocuti.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ParticipacaoRepository : JpaRepository<Participacao, ParticipacaoId> {

    @Query("""
    SELECT p
    FROM Participacao p
    JOIN p.usuario u
    JOIN p.evento e
    WHERE u.idUsuario = :usuarioId
      AND p.isPresente = true
      AND e.statusEvento.idStatusEvento = 2
          AND e.dia >= :dia
""")
    fun listarParticipacoesComFeedback(@Param("usuarioId") usuarioId: Int, @Param("dia") dia: LocalDate
    ): List<Participacao>

    @Query("""
    SELECT p
    FROM Participacao p
    JOIN p.usuario u
    JOIN p.evento e
    WHERE u.idUsuario = :usuarioId
      AND p.isPresente = true
      AND e.statusEvento.idStatusEvento = 2
      AND e.dia < :diaLimite
""")
    fun listarParticipacoesPassadas(@Param("usuarioId") usuarioId: Int, @Param("diaLimite") diaLimite: LocalDate
    ): List<Participacao>

    fun findByUsuarioAndEvento(usuario: Usuario, evento: Evento): Participacao?
}