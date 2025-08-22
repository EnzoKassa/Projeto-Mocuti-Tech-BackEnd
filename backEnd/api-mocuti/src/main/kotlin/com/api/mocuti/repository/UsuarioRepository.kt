package com.api.mocuti.repository

import com.api.mocuti.dto.VisaoGeralUsuariosRequest
import com.api.mocuti.entity.Cargo
import com.api.mocuti.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UsuarioRepository : JpaRepository<Usuario, Int> {

    override fun findAll(): List<Usuario>

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Usuario?

    @Query("select count(u) from Usuario u where u.isAtivo = ?1")
    fun countByIsAtivo(isAtivo: Boolean): Long

    @Query("select u from Usuario u where u.cargo = ?1")
    fun findByCargo(cargo: Cargo): List<Usuario>

    @Query("select count(u) > 0 from Usuario u where u.cpf = ?1")
    fun existsByCpf(cpf: String): Boolean

    @Query(value = "SELECT * FROM visao_geral_usuarios", nativeQuery = true)
    fun buscarVisaoGeralUsuarios(): VisaoGeralUsuariosRequest


}