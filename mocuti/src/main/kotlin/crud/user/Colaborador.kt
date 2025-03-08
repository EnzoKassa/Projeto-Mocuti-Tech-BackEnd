package crud.user

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import java.time.LocalDateTime

data class Colaborador(
    var nome: String? = null,
    var cpf: String? = null,
    var email: String? = null,
    var telefone: String? = null,
    var dtNasc: LocalDate? = null,
    var cargo: String? = null,
    var endereco: String? = null,
    var areaAtuacaoVoluntario: String? = null,
    var necessidadeBeneficiario: String? = null
)
