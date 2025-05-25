package com.api.mocuti.repository

import com.api.mocuti.entity.StatusEvento
import org.springframework.data.jpa.repository.JpaRepository

interface StatusEventoRepository : JpaRepository<StatusEvento, Int> {
}