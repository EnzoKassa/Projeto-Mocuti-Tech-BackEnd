package crud.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/colaborador")
class ColaboradorController {

    val colabs = mutableListOf<Colaborador>(
        Colaborador("Ana", "12345678920", "ana@email.com", "11997845188",
            LocalDate.parse("1972-02-02"),"dona", "Rua das Rosas, 123",
            "", "Pintor")
    )

    //Listar todos os colaboradores
    @GetMapping
    fun lista(
        @RequestParam(required = false) nome: String?,
        @RequestParam(required = false) cpf: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) telefone: String?,
        @RequestParam(required = false) dtNasc: LocalDate?,
        @RequestParam(required = false) cargo: String?,
        @RequestParam(required = false) endereco: String?,
        @RequestParam(required = false) areaAtuacaoVoluntario: String?,
        @RequestParam(required = false) necessidadeBeneficiario: String?
    ): ResponseEntity<List<Colaborador>> {
        if (nome == null && cpf == null && email == null && telefone == null
            && dtNasc == null && cargo == null && endereco == null
            && areaAtuacaoVoluntario == null && necessidadeBeneficiario == null) {
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
        val filtraEndereco = endereco != null

        val listaFiltrada = mutableListOf<Colaborador>()

        if (filtraNome && filtraCpf && filtraDtNasc && filtraCargo
            && filtraEmail && filtraTelefone && filtraEndereco) {
            listaFiltrada.addAll(colabs.filter {
                it.nome == nome && it.cpf == cpf && it.dtNasc == dtNasc && it.cargo == cargo })
        } else if (filtraNome) {
            listaFiltrada.addAll(colabs.filter { it.nome == nome })
        } else if (filtraCpf) {
            listaFiltrada.addAll(colabs.filter { it.cpf == cpf })
        } else if (filtraDtNasc) {
            listaFiltrada.addAll(colabs.filter { it.dtNasc == dtNasc })
        } else if (filtraEmail) {
            listaFiltrada.addAll(colabs.filter { it.email == email })
        }  else if (filtraTelefone) {
            listaFiltrada.addAll(colabs.filter { it.telefone == telefone })
        }  else if (filtraEndereco) {
            listaFiltrada.addAll(colabs.filter { it.endereco == endereco })
        } else {
            listaFiltrada.addAll(colabs.filter { it.cargo == cargo })
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
    @PatchMapping("/{id}/endereco/{novoEndereco}")
    fun mudarEndereco(@PathVariable id: Int, @PathVariable novoEndereco: String): String {
        colabs[id].endereco = novoEndereco
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
        if (id in 0..colabs.size-1){
            return ResponseEntity.status(200).body(colabs[id])
        }
        return ResponseEntity.status(404).build()
    }


}