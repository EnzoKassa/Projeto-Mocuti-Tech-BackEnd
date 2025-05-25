package com.api.mocuti.controller

import com.api.mocuti.entity.Preferencia
import com.api.mocuti.repository.PreferenciaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/preferencias")
class PreferenciaJpaController(var repositorioPreferencia: PreferenciaRepository) {

     @GetMapping
     fun get(): ResponseEntity<List<Preferencia>> {
         val preferencias = repositorioPreferencia.findAll()

         return if (preferencias.isEmpty()) {
             ResponseEntity.status(204).build()
         } else {
             ResponseEntity.status(200).body(preferencias)
         }
     }
}