package mocuti01.repository

import org.springframework.data.jpa.repository.JpaRepository
import mocuti01.entity.Categoria


interface CategoriaRepository : JpaRepository<Categoria, Int> {

    fun findByNomeContains(nome: String): List<Categoria>

}