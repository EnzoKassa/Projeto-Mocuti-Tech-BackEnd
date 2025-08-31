package com.api.mocuti.specification

import com.api.mocuti.dto.EventoFiltroRequest
import com.api.mocuti.entity.Evento
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

/*Specification é um padrão que permite criar queries dinâmicas e combináveis.
 - É como uma receita de critérios de busca.
 - Você define predicados (condições) que serão aplicados na query SQL.
 - Pode combinar múltiplas Specifications usando and, or.
 - Ótimo para filtros opcionais, porque você não precisa escrever uma query nova para cada combinação de parâmetros.

 Se você fizer uma query fixa, precisaria de várias queries diferentes para cada combinação de filtros.
Com Specification, você cria uma Specification para cada filtro e combina dinamicamente, sem repetir código.
 */
class EventoSpecification {
    companion object {
        fun comFiltros(filtro: EventoFiltroRequest): Specification<Evento> {
            //É uma função lambda que cria a query. root = tabela, cb = construtor de critérios usado para criar condições (like, equal, between, etc.).
            return Specification { root, _, cb ->
                //Lista de condições que vamos aplicar na query.
                val predicates = mutableListOf<Predicate>()

//                Se o usuário passou um nome não vazio, cria a condição LIKE '%nome%'.
                filtro.nome?.takeIf { it.isNotBlank() }?.let { nome ->
                    val safeNome = nome.replace("%", "\\%").replace("_", "\\_")
                    predicates.add(
                        //Gera uma condição SQL do tipo: WHERE LOWER(nomeEvento) LIKE '%oficina%' | cb.and(*predicates.toTypedArray())
                        cb.like(cb.lower(root.get("nomeEvento")), "%${safeNome.lowercase()}%")
                    )
                }

                // Filtro por data inicial
                filtro.dataInicio?.let { dataInicio ->
                    predicates.add(
                        // greaterThanOrEqualTo → eventos que começam a partir de dia.
                        cb.greaterThanOrEqualTo(root.get("dia"), dataInicio)
                    )
                }

                // Filtro por data final
                filtro.dataFim?.let { dataFim ->
                    predicates.add(
                        // lessThanOrEqualTo → eventos que ocorrem até dia.
                        cb.lessThanOrEqualTo(root.get("dia"), dataFim)
                    )
                }

                // Categoria
                filtro.categoriaId?.let { categoriaId ->
                    predicates.add(cb.equal(root.get<Any>("categoria").get<Int>("id"), categoriaId))
                }

                // status evento
                filtro.statusEventoId?.let { statusEventoId ->
                    predicates.add(cb.equal(root.get<Any>("statusEvento").get<Int>("id"), statusEventoId))
                }

                cb.and(*predicates.toTypedArray())
            }
        }
    }
}
