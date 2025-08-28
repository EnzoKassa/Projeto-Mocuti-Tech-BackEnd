package com.api.mocuti.dto

interface FaixaEtariaUsuariosAtivosRequest {
    val total_ativos: Int
    val ate_15: Int
    val de_16_a_24: Int
    val de_25_a_40: Int
    val acima_de_40: Int
}