package com.api.mocuti.dto

import java.time.LocalDate
import java.time.LocalTime

interface EventosUsuariosRequest {
    val id_evento: Int
    val nome_evento: String
    val data_evento: LocalDate
    val hora_inicio: LocalTime
    val status_evento: String
    val descricao: String
    val local: String
    val id_usuario: Int
}