package com.api.mocuti.dto

interface ListaPresencaEventoDTO {
    val nomeEvento: String
    val totalInscritos: Int
    val totalPresentes: Int
    val totalAusentes: Int
    val percentualPresenca: Double
    val percentualAusencia: Double
}
