package com.api.mocuti.repository

import com.api.mocuti.entity.PublicoAlvo
import org.springframework.data.jpa.repository.JpaRepository

interface PublicoAlvoRepository : JpaRepository<PublicoAlvo, Int> {
}