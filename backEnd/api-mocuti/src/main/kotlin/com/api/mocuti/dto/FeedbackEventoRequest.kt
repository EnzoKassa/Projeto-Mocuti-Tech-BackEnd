package com.api.mocuti.dto

interface FeedbackEventoRequest {
    val id_feedback: Int
    val descricao_feedback: String?
    val nota: String?
    val id_evento: Int
    val nome_evento: String
    val id_usuario: Int
    val nome_usuario: String
    val email: String
}