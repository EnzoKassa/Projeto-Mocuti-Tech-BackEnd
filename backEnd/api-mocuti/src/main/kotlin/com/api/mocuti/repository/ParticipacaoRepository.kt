package com.api.mocuti.repository

import com.api.mocuti.dto.ParticipacaoResponse
import com.api.mocuti.entity.*
import com.api.mocuti.dto.ConvidadoEventoDTO
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Participacao
import com.api.mocuti.entity.ParticipacaoId
import com.api.mocuti.entity.Usuario
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
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

    @Query("""
        SELECT p
        FROM Participacao p
        JOIN p.evento e
        WHERE p.usuario.idUsuario = :usuarioId
          AND p.isInscrito = true
          AND e.statusEvento.idStatusEvento = 1
""")
    fun findByUsuario_IdUsuarioAndIsInscritoTrueAndEventoStatusAberto(
        @Param("usuarioId") usuarioId: Int
    ): List<Participacao>

    @Query(value = "SELECT * FROM usuarios_inscritos_cargo2 WHERE id_evento = :idEvento", nativeQuery = true)
    fun listarUsuariosInscritosCargo2PorEvento(@Param("idEvento") idEvento: Int): List<Array<Any>>

    @Modifying // Indica que esta query é para modificação (UPDATE, DELETE, INSERT)
    @Transactional // É obrigatório para queries @Modifying
    @Query("""
        UPDATE Participacao p
        SET p.isPresente = :presenca
        WHERE p.id.eventoId = :idEvento
        AND p.id.usuarioId IN :idsUsuarios
    """)
    fun bulkUpdatePresenca(
        @Param("idEvento") idEvento: Int,
        @Param("idsUsuarios") idsUsuarios: List<Int>,
        @Param("presenca") presenca: Boolean
    ): Int // Retorna o número de registros atualizados

    @Query("""
        SELECT COUNT(p)
        FROM Participacao p
        JOIN p.usuario u
        WHERE p.evento.idEvento = :idEvento
          AND u.cargo.idCargo = 2
    """
    )
    fun countUsuariosInscritosCargo2PorEvento(@Param("idEvento") idEvento: Int): Long

    @Query("""
    SELECT new com.api.mocuti.dto.ParticipacaoResponse(
        p.evento.idEvento,
        p.evento.nomeEvento,
        p.evento.dia,
        p.statusInscricao.idStatusInscricao
    )
    FROM Participacao p
    WHERE p.usuario.idUsuario = :usuarioId
      AND p.evento.statusEvento.idStatusEvento = 1
    ORDER BY p.evento.dia DESC
""")
    fun findEventosPresentesPorUsuario(@Param("usuarioId") usuarioId: Int): List<ParticipacaoResponse>


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        """
        UPDATE participacao
        SET fk_inscricao_participacao = :statusInscricaoId,
        is_inscrito = CASE 
            WHEN :statusInscricaoId = 2 THEN 1
            ELSE 0
        END
        WHERE fk_usuario_participacao = :usuarioId
          AND fk_evento_participacao = :eventoId
""", nativeQuery = true
    )
    fun atualizarStatusParticipacao(
        @Param("usuarioId") usuarioId: Int,
        @Param("eventoId") eventoId: Int,
        @Param("statusInscricaoId") statusInscricaoId: Int
    ): Int

    fun findByUsuario_IdUsuarioAndEvento_IdEvento(usuarioId: Int, eventoId: Int): Participacao?

}
