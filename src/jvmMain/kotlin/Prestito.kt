import kotlinx.serialization.Serializable

/**
 * @author Elio0
 * @author Ermanno Oliveri
 * @author C4V4H.exe
 *
 * @param prestato: Boolean
 * @param idUtente: Persona
 * @param dataInizio: String
 * @param dataFine: String
 * @param idCopia: String
 * @param isbn: String
 */
@Serializable
data class Prestito(
    val idPrestito: Int,
    val idCopia: Int,
    val idUtente: Int,
    val dataFine: String?,
    val dataInizio: String?,
    val condizioneIniziale: String?,
    val condizioneFinale: String?,
    var attivo: Boolean
)