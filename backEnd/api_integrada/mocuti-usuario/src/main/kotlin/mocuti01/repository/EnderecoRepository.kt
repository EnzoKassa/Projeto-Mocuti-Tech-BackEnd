package mocuti01.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import mocuti01.entity.Endereco

    interface EnderecoRepository : JpaRepository<Endereco, Int> {

        @Query(
            value = "SELECT e.* FROM endereco e " +
                    "JOIN usuario u ON u.fk_endereco_usuario = e.id_endereco " +
                    "WHERE u.id_usuario = :idUsuario",
            nativeQuery = true
        )
        fun findEnderecoByUsuarioId(@Param("idUsuario") idUsuario: Int): Endereco?


        @Query(
            value = "SELECT e.* FROM endereco e " +
                    "JOIN evento ev ON ev.fk_endereco_evento = e.id_endereco " +  // Ajuste aqui para o nome correto da chave estrangeira
                    "WHERE ev.id_evento = :idEvento",
            nativeQuery = true
        )
        fun findEnderecoByEventoId(@Param("idEvento") idEvento: Int): Endereco?
    }
