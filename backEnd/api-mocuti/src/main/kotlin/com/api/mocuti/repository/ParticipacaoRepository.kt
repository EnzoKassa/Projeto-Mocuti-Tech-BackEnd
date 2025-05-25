package com.api.mocuti.repository

import com.api.mocuti.entity.Participacao
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipacaoRepository : JpaRepository<Participacao, Int> {
}