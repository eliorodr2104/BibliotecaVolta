import kotlinx.serialization.Serializable

@Serializable
data class Persona (val nome: String,
    val cognome: String,
    val mail: String = "$nome.$cognome@volta-alessndria.it",
    val mailAlternativa: String,
    val numero: String
)
