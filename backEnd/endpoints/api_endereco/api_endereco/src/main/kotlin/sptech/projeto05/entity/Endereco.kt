package sptech.projeto05.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
data class Endereco(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id_endereco:Int?, @field:NotNull
    @field:Size(max = 8) var CEP: String? = null,
    @field:NotBlank var logradouro: String? = null,
    @field:NotNull var numero: Int = 0,
    @field:Size(max = 45) var complemento:String? = null,
    @field:Size(max = 2) var UF: String? = null,
    @field:Size(max = 45)var estado:String? = null,
    @field:Size(max = 125) var bairro:String? = null
) {
constructor(): this(null, null)
}