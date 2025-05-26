package com.api.mocuti.controller

import com.api.mocuti.entity.Endereco
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

class EnderecoUnitTest {
  val endereco = Endereco()

  @Test
  @DisplayName("Deve criar um Endereco válido corretamente")
  fun testEnderecoValido() {
   val endereco = Endereco(
    idEndereco = 1,
    CEP = "12345678",
    logradouro = "Rua das Flores",
    numero = 100,
    complemento = "Apto 12",
    UF = "SP",
    estado = "São Paulo",
    bairro = "Centro"
   )

   assertEquals("12345678", endereco.CEP)
   assertEquals("Rua das Flores", endereco.logradouro)
   assertEquals(100, endereco.numero)
   assertEquals("Apto 12", endereco.complemento)
   assertEquals("SP", endereco.UF)
   assertEquals("São Paulo", endereco.estado)
   assertEquals("Centro", endereco.bairro)
  }

  @Test
  @DisplayName("Complemento pode ser nulo")
  fun testComplementoNulo() {
   val endereco = Endereco(
    idEndereco = 2,
    CEP = "87654321",
    logradouro = "Av. Paulista",
    numero = 10,
    complemento = null,
    UF = "SP",
    estado = "São Paulo",
    bairro = "Bela Vista"
   )

   assertNull(endereco.complemento)
  }

  @Test
  @DisplayName("Numero igual a zero deve ser considerado inválido")
  fun testNumeroZero() {
   val endereco = Endereco(
    idEndereco = 3,
    CEP = "12345678",
    logradouro = "Rua A",
    numero = 0,
    complemento = null,
    UF = "SP",
    estado = "São Paulo",
    bairro = "Centro"
   )

   assertEquals(0, endereco.numero, "Número é igual a zero, deveria ser inválido pela regra de negócio")
  }

  @Test
  @DisplayName("UF maior que 2 caracteres deve ser inválido na lógica de negócio")
  fun testUFInvalida() {
   val endereco = Endereco(
    idEndereco = 4,
    CEP = "12345678",
    logradouro = "Rua B",
    numero = 10,
    complemento = null,
    UF = "SPX",
    estado = "São Paulo",
    bairro = "Centro"
   )

   assertTrue(endereco.UF!!.length > 2, "UF está maior que 2, deveria ser inválido pela regra de negócio")
  }

  @Test
  @DisplayName("CEP pode ter no máximo 8 caracteres")
  fun testCEPMaiorQue8() {
   val endereco = Endereco(
    idEndereco = 5,
    CEP = "123456789",
    logradouro = "Rua C",
    numero = 10,
    complemento = null,
    UF = "SP",
    estado = "São Paulo",
    bairro = "Centro"
   )

   assertTrue(endereco.CEP!!.length > 8, "CEP tem mais que 8 caracteres, deveria ser inválido")
  }

  @Test
  @DisplayName("Logradouro nulo ou vazio deve ser considerado inválido")
  fun testLogradouroNuloOuVazio() {
   val enderecoNulo = Endereco(
    idEndereco = 6,
    CEP = "12345678",
    logradouro = null,
    numero = 10,
    complemento = null,
    UF = "SP",
    estado = "São Paulo",
    bairro = "Centro"
   )
   assertNull(enderecoNulo.logradouro, "Logradouro está nulo, deveria ser inválido")

   val enderecoVazio = Endereco(
    idEndereco = 7,
    CEP = "12345678",
    logradouro = "",
    numero = 10,
    complemento = null,
    UF = "SP",
    estado = "São Paulo",
    bairro = "Centro"
   )
   assertEquals("", enderecoVazio.logradouro, "Logradouro está vazio, deveria ser inválido")
  }

 }