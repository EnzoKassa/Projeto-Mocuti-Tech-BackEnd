package com.api.mocuti.dto

interface VisaoGeralUsuariosRequest {
    val total_usuarios_ativos: Int
    val total_usuarios_inativos: Int
    val total_m1_ativos: Int
    val total_m1_inativos: Int
    val total_m2_ativos: Int
    val total_m2_inativos: Int
    val total_beneficiarios_ativos: Int
    val total_beneficiarios_inativos: Int
}
