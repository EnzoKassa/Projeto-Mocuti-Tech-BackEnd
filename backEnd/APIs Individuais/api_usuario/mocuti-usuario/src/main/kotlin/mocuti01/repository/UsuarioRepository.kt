package mocuti01.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import mocuti01.entity.Usuario

interface UsuarioRepository : JpaRepository<Usuario, Int> {

    override fun findAll(): List<Usuario>

    fun findByEmailAndSenha(email: String, senha: String): Usuario?

    fun existsByEmail(email: String): Boolean

    @Transactional
    @Modifying
    @Query("update Usuario u set u.isAtivo = false, u.dtDesativacao = current_date where u.idUsuario = ?1")
    fun desativarUsuario(idUsuario: Int): Int

    @Query("select count(u) from Usuario u where u.isAtivo = ?1")
    fun countByIsAtivo(isAtivo: Boolean): Long

    @Query("select u from Usuario u where u.cargoInt = ?1")
    abstract fun findByCargoInt(cargoInt: Int): List<Usuario>?

    @Query("select count(u) > 0 from Usuario u where u.cpf = ?1")
    fun existsByCpf(cpf: String): Boolean

    @Query("select u.genero, count(u) from Usuario u where u.genero in ('Masculino', 'Feminino') group by u.genero")
    fun countByGenero(string: String): List<Array<Any>>
}