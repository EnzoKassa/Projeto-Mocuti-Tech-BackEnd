package com.api.mocuti.repository

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Categoria
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoriaRepository : JpaRepository<Categoria, Int> {

    @Query(
        value = "SELECT * FROM rank_categoria",
        nativeQuery = true
    )
    fun buscarRanking(): List<RankCategoriaRequest>
}