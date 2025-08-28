package com.api.mocuti.service

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.repository.CategoriaRepository
import org.springframework.stereotype.Service

@Service
class CategoriaService
    (val categoriaRepository: CategoriaRepository) {

    fun getRanking(): List<RankCategoriaRequest> {
      return categoriaRepository.buscarRanking()
    }
}