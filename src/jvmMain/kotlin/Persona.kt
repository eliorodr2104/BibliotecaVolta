import kotlinx.serialization.Serializable

/**
 * @author Elio0
 * data class persona
 *
 * @param nome: String
 * @param cognome: String
 * @param mail: String = "$nome.$cognome@volta-alessndria.it"
 * @param mailAlternativa: String
 * @param numero: String
 */
@Serializable
data class Persona (val nome: String,
    val cognome: String,
    val mail: String = "$nome.$cognome@volta-alessndria.it",
    val mailAlternativa: String,
    val numero: String?
)
