package com.api.mocuti.repository

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Cargo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CargoRepository : JpaRepository<Cargo, Int> {

    @Query("SELECT * FROM rank_categoria", nativeQuery = true)
    fun buscarRanking(): List<RankCategoriaRequest>
}