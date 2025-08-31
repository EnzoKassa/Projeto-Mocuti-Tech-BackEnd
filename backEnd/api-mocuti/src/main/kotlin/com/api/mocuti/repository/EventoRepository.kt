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

}