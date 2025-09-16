package com.api.mocuti.repository

import com.api.mocuti.entity.Preferencia
import com.api.mocuti.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PreferenciaRepository : JpaRepository<Preferencia, Int> {
    @Query("SELECT p.usuario FROM Preferencia p WHERE p.categoria.idCategoria = :idCategoria")
    fun findUsuariosByIdCategoria(@Param("idCategoria") idCategoria: Int): List<Usuario>
}