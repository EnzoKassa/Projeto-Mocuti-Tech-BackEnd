package com.api.mocuti.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "convite")
data class Convite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_convite")
    val idConvite: Int = 0,

    @ManyToOne
    @JoinColumn(name = "fk_evento_convite", nullable = false)
    val evento: Evento,

    @ManyToOne
    @JoinColumn(name = "fk_usuario_convidado", nullable = false)
    val usuarioConvidado: Usuario,

    @ManyToOne
    @JoinColumn(name = "fk_usuario_remetente", nullable = true)
    val usuarioRemetente: Usuario? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status_convite", nullable = false)
    var statusConvite: StatusConvite = StatusConvite.EM_ANDAMENTO,

    @Column(name = "data_convite", nullable = false)
    val dataConvite: LocalDate = LocalDate.now()
)