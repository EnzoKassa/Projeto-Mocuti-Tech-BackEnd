package sptech.projeto05.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import kotlin.reflect.KClass

object FileUtils {

    val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    fun readJsonFile(fileName: String): String {
        return FileUtils::class.java.getResource("/$fileName").readText()
    }

    fun <T: Any> readJsonFileAsObject(fileName: String, targetClass:KClass<T>): T {
        return objectMapper.readValue(readJsonFile(fileName), targetClass.javaObjectType)
    }
}