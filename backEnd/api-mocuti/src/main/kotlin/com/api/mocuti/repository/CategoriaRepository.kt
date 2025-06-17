package com.api.mocuti.repository

import com.api.mocuti.entity.Categoria
import org.springframework.data.jpa.repository.JpaRepository

interface CategoriaRepository : JpaRepository<Categoria, Int> {

    fun findByNomeContains(nome: String): List<Categoria>
}