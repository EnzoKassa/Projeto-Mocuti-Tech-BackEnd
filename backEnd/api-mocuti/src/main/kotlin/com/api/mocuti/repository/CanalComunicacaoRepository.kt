package com.api.mocuti.repository

import com.api.mocuti.entity.CanalComunicacao
import org.springframework.data.jpa.repository.JpaRepository

interface CanalComunicacaoRepository : JpaRepository<CanalComunicacao, Int> {
}