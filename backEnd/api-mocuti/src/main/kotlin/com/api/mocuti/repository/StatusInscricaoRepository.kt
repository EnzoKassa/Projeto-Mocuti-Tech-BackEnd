package com.api.mocuti.repository

import com.api.mocuti.entity.StatusInscricao
import org.springframework.data.jpa.repository.JpaRepository

interface StatusInscricaoRepository : JpaRepository<StatusInscricao, Int> {
}