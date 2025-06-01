package com.api.mocuti.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

// Diz ao JPA que essa classe pode ser embutida em uma entidade como parte da chave primária
@Embeddable
data class ParticipacaoId(
    @Column(name = "fk_usuario_participacao")
    val usuarioId: Int = 0,

    @Column(name = "fk_evento_participacao")
    val eventoId: Int = 0
) : Serializable  // Necessário para permitir que o JPA serialize essa chave em cache ou sessões
