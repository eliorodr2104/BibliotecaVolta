import kotlinx.serialization.Serializable

/**
 * @author C4V4.exe
 *
 * Data class Libro
 * @param isbn: String
 * @param titolo: String
 * @param sottotitolo: String
 * @param lingua: String
 * @param casaEditrice: String
 * @param autore: String
 * @param annoPubblicazione: String
 * @param idCategorie: ArrayList<Int>
 * @param idGenere: String
 * @param descrizione: String
 * @param np: Int
 * @param image: ByteArray
 */
@Serializable
data class DatiLibro(
    val isbn: String,
    val titolo: String,
    val sottotitolo: String?,
    val lingua: String,
    val casaEditrice: String?,
    val autore: String,
    val annoPubblicazione: String?,
    val idCategorie: ArrayList<Int>,
    val idGenere: Int,
    val descrizione: String?,
    val np: Int,
    val image: String?
)
