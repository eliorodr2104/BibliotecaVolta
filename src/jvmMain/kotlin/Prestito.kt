import kotlinx.serialization.Serializable

@Serializable
data class Prestito(var prestato: Boolean,
                    val persona: Persona?,
                    val dataInizio: String?,
                    val dataFine: String?,
                    val idCopia: Long,
                    val isbn: String?)