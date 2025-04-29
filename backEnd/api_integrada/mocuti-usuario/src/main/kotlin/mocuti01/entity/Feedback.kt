package mocuti01.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity // do pacote jakarta.persistence
data class Feedback(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int?,
    var nota: Boolean? = false,
    @Lob
    @Column(nullable = false)
    val comentario: String?


) {
    // O JPA exige que exista um construtor vazio nas Entidades
    constructor() : this(null, null,null)
    /*
    Aqui dizemos que sempre que o construtor vazio for invocado,
    será criado uma Musica com id, nome e interprete nulos
     */




}
