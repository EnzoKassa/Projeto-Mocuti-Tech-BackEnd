package crud.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/colaborador")
class ColaboradorController {

    val colabs = mutableListOf<Colaborador>(
        Colaborador(
            "Ana", "12345678920", "ana@email.com", "11997845188",
            LocalDate.parse("1972-02-02"), "dona", "02678-455", "Avenida Paulista",
            574, "", "", "Pintor"
        )
    )

    //Listar todos os colaboradores
    @GetMapping
    fun lista(
        @RequestParam (required = false) nome: String?,
        @RequestParam (required = false) cpf: String?,
        @RequestParam (required = false) email: String?,
        @RequestParam (required = false) telefone: String?,
        @RequestParam (required = false) dtNasc: LocalDate?,
        @RequestParam (required = false) cargo: String?,

        @RequestParam (required = false) cep: String?,
        @RequestParam (required = false) rua: String?,
        @RequestParam (required = false) numero: Int?,
        @RequestParam(required = false) complemento: String?,

        @RequestParam(required = false) areaAtuacaoVoluntario: String?,
        @RequestParam(required = false) necessidadeBeneficiario: String?
    ): ResponseEntity<List<Colaborador>> {
        if (complemento == null && areaAtuacaoVoluntario == null && necessidadeBeneficiario == null) {
            if (colabs.isEmpty()) {
                return ResponseEntity.status(204).build()
            }
            return ResponseEntity.status(200).body(colabs)
        }

        val filtraNome = nome != null
        val filtraCpf = cpf != null
        val filtraEmail = email != null
        val filtraTelefone = telefone != null
        val filtraDtNasc = dtNasc != null
        val filtraCargo = cargo != null
        val filtraCep = cep != null
        val filtraRua = rua != null
        val filtraNumero = numero != null

        val filtraComplemento = complemento != null
        val filtraVoluntario = areaAtuacaoVoluntario != null
        val filtraBeneficiario = necessidadeBeneficiario != null

        val listaFiltrada = mutableListOf<Colaborador>()

        if (filtraNome && filtraCpf && filtraEmail && filtraTelefone
            && filtraDtNasc && filtraCargo && filtraCep && filtraRua
            && filtraNumero && filtraVoluntario && filtraBeneficiario
            && filtraComplemento
        ) {
            listaFiltrada.addAll(colabs.filter {
                it.nome == nome &&
                        it.cpf == cpf &&
                        it.email == email &&
                        it.telefone == telefone &&
                        it.dtNasc == dtNasc &&
                        it.cargo == cargo &&
                        it.cep == cep &&
                        it.rua == rua &&
                        it.numero == numero &&
                        it.complemento == complemento &&
                        it.areaAtuacaoVoluntario == areaAtuacaoVoluntario &&
                        it.necessidadeBeneficiario == necessidadeBeneficiario
            })
        }
        if (listaFiltrada.isEmpty()) {
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(200).body(listaFiltrada)
    }

    //Adicionar um novo Colaborador
    @PostMapping
    fun criar(@RequestBody novoColaborador: Colaborador): ResponseEntity<Colaborador> {
        colabs.add(novoColaborador)

        return ResponseEntity.status(201).body(novoColaborador)
    }

    //Atualizar os dados de um Colaborador
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody ColaboradorAtualizado: Colaborador): String {
        colabs[id] = ColaboradorAtualizado
        return "Colaborador atualizado com sucesso!"
    }

    //Deletando um Administrador
    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): String {
        colabs.removeAt(id)
        return "Colaborador excluído com sucesso"
    }

    //Atualizando apenas um dado (cargo) do Colaborador
    @PatchMapping("/{id}/cargo/{novoCargo}")
    fun mudarCargo(@PathVariable id: Int, @PathVariable novoCargo: String): String {
        colabs[id].cargo = novoCargo
        return "Cargo atualizado com sucesso"
    }

    //Atualizando apenas um dado (endereco) do Colaborador
    @PatchMapping("/{id}/endereco/{novoCep}/{novoRua}/{novoNumero}")
    fun alterarEndereco(
        @PathVariable id: Int, @PathVariable novoCep: String,
        @PathVariable novoRua: String, @PathVariable novoNumero: Int
    ): String {
        colabs[id].cep = novoCep
        colabs[id].rua = novoRua
        colabs[id].numero = novoNumero
        return "Endereço atualizado com sucesso"
    }

    //Atualizando apenas um dado (telefone) do Colaborador
    @PatchMapping("/{id}/telefone/{novoTelefone}")
    fun mudarTelefone(@PathVariable id: Int, @PathVariable novoTelefone: String): String {
        colabs[id].telefone = novoTelefone
        return "Telefone atualizado com sucesso"
    }

    //Atualizando apenas um dado (necessidade do beneficiario) do Colaborador
    @PatchMapping("/{id}/necessidade/{novaNecessidade}")
    fun mudarNecessidade(@PathVariable id: Int, @PathVariable novaNecessidade: String): String {
        colabs[id].necessidadeBeneficiario = novaNecessidade
        return "Necessidade do beneficiário atualizada com sucesso"
    }

    //Atualizando apenas um dado (Area de atuação do beneficiario) do Colaborador
    @PatchMapping("/{id}/atuacao/{novaAtuacao}")
    fun mudarAtuacao(@PathVariable id: Int, @PathVariable novaAtuacao: String): String {
        colabs[id].areaAtuacaoVoluntario = novaAtuacao
        return "Área de atuação atualizada com sucesso"
    }

    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Int): ResponseEntity<Colaborador> {
        if (id in 0..colabs.size - 1) {
            return ResponseEntity.status(200).body(colabs[id])
        }
        return ResponseEntity.status(404).build()
    }


}