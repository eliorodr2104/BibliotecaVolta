import kotlinx.serialization.Serializable

/**
 * @author Elio0
 * data class persona
 *
 * @param nome: String
 * @param cognome: String
 * @param mail: String = "$nome.$cognome@volta-alessndria.it"
 * @param mailAlternativa: String?
 * @param numero: String
 */
@Serializable
data class Utente(
    val idUtente: Int,
    val nome: String,
    val cognome: String,
    val numero: String?,
    val mailAlternativa: String?,
    val grado: Int = 0,
    val mail: String = "$nome.$cognome@volta-alessndria.it",
    val preferiti: String?
)
