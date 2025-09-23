package com.api.mocuti.service

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Categoria
import com.api.mocuti.repository.CategoriaRepository
import org.springframework.stereotype.Service

@Service
class CategoriaService(
    private val categoriaRepository: CategoriaRepository
) {
    fun listarTodos(): List<Categoria> =
        categoriaRepository.findAll()

    fun buscarPorId(id: Int): Categoria? =
        categoriaRepository.findById(id).orElse(null)

    fun deletar(id: Int): Boolean {
        return if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun salvar(categoria: Categoria): Categoria =
        categoriaRepository.save(categoria)

    fun atualizar(id: Int, categoriaAtualizada: Categoria): Categoria? {
        return if (categoriaRepository.existsById(id)) {
            categoriaAtualizada.idCategoria = id
            categoriaRepository.save(categoriaAtualizada)
        } else {
            null
        }
    }

    fun getRanking(): List<RankCategoriaRequest> =
        categoriaRepository.buscarRanking()
}
