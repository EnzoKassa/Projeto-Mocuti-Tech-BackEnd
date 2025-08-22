package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "visao_geral_usuarios_view")

data class VisaoGeralUsuariosView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id: Int,

    @Column(name = "total_usuarios_ativos")
    var totalUsuariosAtivos: Int,

    @Column(name = "total_usuarios_inativos")
    var totalUsuariosInativos: Int,

    @Column(name = "total_m1_ativos")
    var totalM1Ativos: Int,

    @Column(name = "total_m1_inativos")
    var totalM1Inativos: Int,

    @Column(name = "total_m2_ativos")
    var totalM2Ativos: Int,

    @Column(name = "total_m2_inativos")
    val totalM2Inativos: Int,

    @Column(name = "total_beneficiarios_ativos")
    val totalBeneficiariosAtivos: Int,

    @Column(name = "total_beneficiarios_inativos")
    var totalBeneficiariosInativos: Int
)

