import kotlinx.serialization.Serializable

@Serializable
data class CopiaLibro(
    var isbn: String,
    var idCopia: String,
    var condizioni: String,
    var posizione: String?,
    var prestito: Prestito
)