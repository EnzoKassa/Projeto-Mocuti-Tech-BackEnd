package sptech.projeto05.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*



    @Entity // do pacote jakarta.persistence
    data class Categoria(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Int?,
        @field:NotBlank @field:Size(min = 2, max = 20) var nome: String?,
        @field:NotBlank var descricao: String?

    ) {

        constructor() : this(null, null, null)
    }

