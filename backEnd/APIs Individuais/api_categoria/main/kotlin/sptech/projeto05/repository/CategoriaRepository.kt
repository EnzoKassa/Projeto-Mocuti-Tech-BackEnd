package sptech.projeto05.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import sptech.projeto05.entity.Categoria


interface CategoriaRepository : JpaRepository<Categoria, Int> {

    fun findByNomeContains(nome: String): List<Categoria>

}