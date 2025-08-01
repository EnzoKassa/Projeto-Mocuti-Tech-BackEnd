package com.api.mocuti.repository

import com.api.mocuti.entity.Categoria
import org.springframework.data.jpa.repository.JpaRepository

interface CategoriaRepository : JpaRepository<Categoria, Int> {}