package mocuti01.repository

import mocuti01.entity.Evento
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.sql.Time
import java.time.LocalDate

interface EventoRepository : JpaRepository<Evento, Int> {
    @Transactional
    @Modifying
    @Query("update Evento e set e.dia = ?2, e.hora_inicio = ?3, e.hora_fim = ?4 where e.id_evento = ?1", nativeQuery = true)
    fun atualizarDiaHora(id: Int, dia: LocalDate, horaInicio: Time, horaFim: Time): Int
}