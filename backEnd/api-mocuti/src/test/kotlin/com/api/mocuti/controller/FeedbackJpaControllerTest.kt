package com.api.mocuti.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.api.mocuti.entity.Feedback
import com.api.mocuti.repository.FeedbackRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(FeedbackJpaController::class)
class FeedbackJpaControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var repositorio: FeedbackRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `deve retornar 200 e lista de feedbacks quando existirem feedbacks`() {
        val feedbacks = listOf(
            Feedback(id = 1, comentario = "Ótimo serviço"),
            Feedback(id = 2, comentario = "Muito bom")
        )
        `when`(repositorio.findAll()).thenReturn(feedbacks)

        mockMvc.perform(get("/feedback").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(feedbacks.size))
    }

    @Test
    fun `deve retornar 204 quando nao houver nenhum feedback`() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        mockMvc.perform(get("/feedback"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deve retornar 200 e o feedback quando o ID existir`() {
        val feedback = Feedback(id = 1, comentario = "Ótimo serviço")
        `when`(repositorio.findById(1)).thenReturn(java.util.Optional.of(feedback))

        mockMvc.perform(get("/feedback/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.comentario").value("Ótimo serviço"))
    }

    @Test
    fun `deve retornar 404 quando o ID nao existir`() {
        `when`(repositorio.findById(1)).thenReturn(java.util.Optional.empty())

        mockMvc.perform(get("/feedback/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve retornar 204 e deletar o feedback quando o ID existir`() {
        `when`(repositorio.existsById(1)).thenReturn(true)

        mockMvc.perform(delete("/feedback/1"))
            .andExpect(status().isNoContent)

        verify(repositorio, times(1)).deleteById(1)
    }

    @Test
    fun `deve retornar 404 quando o ID nao existir ao deletar`() {
        `when`(repositorio.existsById(1)).thenReturn(false)

        mockMvc.perform(delete("/feedback/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve retornar 201 e o feedback criado quando os dados forem validos`() {
        val feedback = Feedback(id = 1, comentario = "Ótimo serviço")
        `when`(repositorio.save(any(Feedback::class.java))).thenReturn(feedback)

        mockMvc.perform(
            post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedback))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.comentario").value("Ótimo serviço"))
    }

    @Test
    fun `deve retornar 400 quando os dados forem invalidos`() {
        val feedback = Feedback(id = 1, comentario = "") // Comentário inválido

        mockMvc.perform(
            post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedback))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `deve retornar 200 e atualizar o feedback quando o ID existir`() {
        val feedbackAtualizado = Feedback(id = 1, comentario = "Serviço atualizado")
        `when`(repositorio.existsById(1)).thenReturn(true)
        `when`(repositorio.save(any(Feedback::class.java))).thenReturn(feedbackAtualizado)

        mockMvc.perform(
            put("/feedback/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackAtualizado))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.comentario").value("Serviço atualizado"))
    }

    @Test
    fun `deve retornar 404 quando tentar atualizar um ID inexistente`() {
        val feedbackAtualizado = Feedback(id = 1, comentario = "Serviço atualizado")
        `when`(repositorio.existsById(1)).thenReturn(false)

        mockMvc.perform(
            put("/feedback/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackAtualizado))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve retornar 400 quando os dados enviados forem invalidos`() {
        val feedback = Feedback(id = 1, comentario = "") // Comentário inválido

        mockMvc.perform(
            put("/feedback/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedback))
        )
            .andExpect(status().isNotFound)

        verify(repositorio, times(0)).save(any(Feedback::class.java))
    }
}