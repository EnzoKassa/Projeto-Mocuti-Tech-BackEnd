package com.api.mocuti.repository

import com.api.mocuti.dto.*
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

    @Query(
        value = "SELECT * FROM inscricoes_mes_durante_ano",
        nativeQuery = true
    )
    fun getInscricoesMesDuranteAno(): List<InscricoesMesDuranteAnoRequest>

    @Query(
        value = "SELECT * FROM publico_alvo_genero",
        nativeQuery = true
    )
    fun getPublicoAlvoGenero(): List<PublicoAlvoGeneroRequest>

    @Query(
        value = "SELECT * FROM faixa_etaria_usuarios_ativos",
        nativeQuery = true
    )
    fun getFaixaEtariaUsuariosAtivos(): List<FaixaEtariaUsuariosAtivosRequest>


    @Query(
        value = """
            SELECT 
                nome_evento AS nomeEvento,
                total_inscritos AS totalInscritos,
                total_presentes AS totalPresentes,
                total_ausentes AS totalAusentes,
                percentual_presenca AS percentualPresenca,
                percentual_ausencia AS percentualAusencia
            FROM lista_presenca_evento
            WHERE id_evento = :idEvento
        """,
        nativeQuery = true
    )
    fun findByEventoId(idEvento: Long): ListaPresencaEventoDTO?
}