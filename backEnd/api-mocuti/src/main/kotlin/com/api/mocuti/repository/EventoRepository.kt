package com.api.mocuti.repository

import com.api.mocuti.dto.EventosUsuariosRequest
import com.api.mocuti.entity.Evento
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalTime
// Herdar JpaSpecificationExecutor permite usar o método: findAll(EventoSpecification.comFiltros(filtro))
interface EventoRepository : JpaRepository<Evento, Int>, JpaSpecificationExecutor<Evento> {
    @Transactional
    @Modifying
    @Query(
        "update Evento e set e.dia = ?2, e.hora_inicio = ?3, e.hora_fim = ?4 where e.id_evento = ?1",
        nativeQuery = true
    )
    fun atualizarDiaHora(id: Int, dia: LocalDate, horaInicio: LocalTime, horaFim: LocalTime): Int

    @Query(
        value = "SELECT * FROM eventos_usuario",
        nativeQuery = true
    )
    fun getEventosUsuario(): List<EventosUsuariosRequest>

    @Query(
        value = "SELECT * FROM eventos_usuario WHERE id_usuario = ?1",
        nativeQuery = true
    )
    fun getEventosUsuarioPorId(idUsuario: Int): EventosUsuariosRequest

    fun findByCategoria_IdCategoria(categoriaId: Int): List<Evento>

    fun findByStatusEvento_IdStatusEvento(statusEventoId: Int): List<Evento>


    @Modifying
    @Transactional
    @Query(
        """
        UPDATE mocuti.evento
        SET fk_status_evento =
            ( -- Subconsulta ou expressão CASE para o NOVO STATUS
                CASE
                    -- Prioridade 1: Em Andamento (3)
                    WHEN TIMESTAMP(dia, hora_inicio) <= CURRENT_TIMESTAMP() AND TIMESTAMP(dia, hora_fim) > CURRENT_TIMESTAMP() THEN 3
                    
                    -- Prioridade 2: Encerrado (2) - Pelo tempo
                    WHEN TIMESTAMP(dia, hora_fim) <= CURRENT_TIMESTAMP() THEN 2
                    
                    -- Prioridade 3: Aberto (1) - Se o tempo de início ainda não chegou
                    WHEN TIMESTAMP(dia, hora_inicio) > CURRENT_TIMESTAMP() THEN 1
                    
                    -- Fallback: Se nenhuma das condições acima for atendida, mantém o status atual (segurança)
                    ELSE fk_status_evento 
                END
            )
        WHERE hora_inicio IS NOT NULL
          AND hora_fim IS NOT NULL
          AND dia IS NOT NULL
          -- Garante que eventos já encerrados manualmente não sejam reabertos
          AND fk_status_evento != 2 
          -- ATUALIZAÇÃO SÓ OCORRE SE O STATUS CALCULADO FOR DIFERENTE DO STATUS ATUAL
          AND fk_status_evento != ( -- Recalcula o status esperado para comparação
                CASE
                    WHEN TIMESTAMP(dia, hora_inicio) <= CURRENT_TIMESTAMP() AND TIMESTAMP(dia, hora_fim) > CURRENT_TIMESTAMP() THEN 3
                    WHEN TIMESTAMP(dia, hora_fim) <= CURRENT_TIMESTAMP() THEN 2
                    WHEN TIMESTAMP(dia, hora_inicio) > CURRENT_TIMESTAMP() THEN 1
                    ELSE fk_status_evento
                END
            )
        """,
        nativeQuery = true
    )
    fun atualizarStatusEventos(): Int
}
