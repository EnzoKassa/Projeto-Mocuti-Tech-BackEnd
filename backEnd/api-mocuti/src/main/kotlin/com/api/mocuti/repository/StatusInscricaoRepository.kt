package com.api.mocuti.repository

import com.api.mocuti.entity.StatusInscricao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StatusInscricaoRepository : JpaRepository<StatusInscricao, Int> {

    @Query("SELECT s FROM StatusInscricao s WHERE s.tipoInscricao = :tipo")
    fun findByTipo(tipo: String): StatusInscricao?
}