package com.api.mocuti.repository

import com.api.mocuti.entity.Endereco
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface EnderecoRepository : JpaRepository<Endereco, Int> {

    @Query(
        value = "SELECT e.* FROM endereco e " +
                "JOIN usuario u ON u.fk_endereco_usuario = e.id_endereco " +
                "WHERE u.id_usuario = :idUsuario",
        nativeQuery = true
    )
    fun findByUsuarioId(idUsuario: Int): Endereco?

    @Query(
        value = "SELECT e.* FROM endereco e " +
                "JOIN evento ev ON ev.fk_endereco_evento = e.id_endereco " +  // Ajuste aqui para o nome correto da chave estrangeira
                "WHERE ev.id_evento = :idEvento",
        nativeQuery = true
    )
    fun findEnderecoByEventoId(@Param("idEvento") idEvento: Int): Endereco?

    @Query(
        value = "SELECT DISTINCT e.* FROM Endereco e JOIN Evento ev ON e.id_endereco = ev.fk_endereco_evento;",
        nativeQuery = true
    )
    fun findDistinctEnderecosComEventos(): List<Endereco?>?

}
