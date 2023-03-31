import kotlinx.serialization.Serializable

/**
 * @author elio0
 *
 * @param isbn: String
 * @param idCopia: String
 * @param condizioni: String
 * @param posizione: String
 * @param prestito: Prestito
 */
@Serializable
data class CopiaLibro(
    var isbn: String,
    var idCopia: String,
    var condizioni: String,
    var posizione: String?,
    var prestito: Prestito
)