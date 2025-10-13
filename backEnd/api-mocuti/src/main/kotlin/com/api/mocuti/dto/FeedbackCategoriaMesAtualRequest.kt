package com.api.mocuti.dto

interface FeedbackCategoriaMesAtualRequest {
    val qtd_positivos: Int
    val qtd_total: Int
    val qtd_negativos: Int
    val categoria: String
}