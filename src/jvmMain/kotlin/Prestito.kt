import kotlinx.serialization.Serializable

/**
 * @author Elio0
 * @author Ermanno Oliveri
 * @author C4V4H.exe
 *
 * @param prestato: Boolean
 * @param persona: Persona
 * @param dataInizio: String
 * @param dataFine: String
 * @param idCopia: String
 * @param isbn: String
 */
@Serializable
data class Prestito(var prestato: Boolean,
                    val persona: Persona?,
                    val dataInizio: String?,
                    val dataFine: String?,
                    val idCopia: String,
                    val isbn: String?)