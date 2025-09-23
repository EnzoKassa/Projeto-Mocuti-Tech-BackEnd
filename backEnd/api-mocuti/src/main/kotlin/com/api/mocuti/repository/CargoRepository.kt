package com.api.mocuti.repository

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Cargo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CargoRepository : JpaRepository<Cargo, Int> {

    @Query("SELECT new com.api.mocuti.dto.RankCategoriaRequest(c.nome, COUNT(v)) " +
            "FROM Categoria c JOIN c.votos v GROUP BY c.nome ORDER BY COUNT(v) DESC")
    fun buscarRanking(): List<RankCategoriaRequest>
}