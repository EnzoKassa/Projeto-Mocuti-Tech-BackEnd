package com.api.mocuti.service

import com.api.mocuti.dto.PreferenciaRequest
import com.api.mocuti.entity.Preferencia
import com.api.mocuti.repository.CategoriaRepository
import com.api.mocuti.repository.PreferenciaRepository
import com.api.mocuti.repository.UsuarioRepository
import org.springframework.stereotype.Service

@Service
class PreferenciaService(
    private val usuarioRepository: UsuarioRepository,
    private val categoriaRepository: CategoriaRepository,
    private val preferenciaRepository: PreferenciaRepository
) {
    fun salvar(req: PreferenciaRequest): Preferencia {
        val usuario = usuarioRepository.findById(req.idUsuario)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        val categoria = categoriaRepository.findById(req.idCategoria)
            .orElseThrow { RuntimeException("Categoria não encontrada") }

        val preferencia = Preferencia(
            usuario = usuario,
            categoria = categoria
        )
        return preferenciaRepository.save(preferencia)
    }
}
