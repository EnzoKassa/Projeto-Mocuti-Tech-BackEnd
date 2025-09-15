package com.api.mocuti.repository

import com.api.mocuti.dto.FeedbackCategoriaMesAtualRequest
import com.api.mocuti.dto.FeedbackEventoRequest
import com.api.mocuti.dto.FeedbacksPorCategoriaRequest
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.Feedback
import com.api.mocuti.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FeedbackRepository : JpaRepository<Feedback, Int> {

    @Query(
        value = "SELECT * FROM feedbacks_por_categoria",
        nativeQuery = true
    )
    fun getFeedbacksPorCategoria(): List<FeedbacksPorCategoriaRequest>

    @Query(
        value = "SELECT * FROM feedback_categoria_mes_atual",
        nativeQuery = true
    )
    fun getFeedbackCategoriaMesAtual(): List<FeedbackCategoriaMesAtualRequest>

    @Query(
        value = "SELECT * FROM feedback_evento",
        nativeQuery = true
    )
    fun getFeedbackEvento(): List<FeedbackEventoRequest>

    fun findByUsuarioAndEvento(usuario: Usuario, evento: Evento): Feedback?
}