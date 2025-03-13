package crud.user

import java.time.LocalDate

data class Colaborador(
    var nome: String,
    var cpf: String,
    var email: String,
    var telefone: String,
    var dtNasc: LocalDate,
    var cargo: String,

    //Endereço
    var cep: String,
    var rua: String,
    var numero: Int,
    var complemento: String? = null,

    var areaAtuacaoVoluntario: String? = null,
    var necessidadeBeneficiario: String? = null
)
