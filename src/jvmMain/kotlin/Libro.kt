import kotlinx.serialization.Serializable

/**
 * @author C4V4.exe
 * @author Elio0
 *
 * Data class Libro
 * @param isbn: String
 * @param titolo: String
 * @param sottotitolo: String
 * @param lingua: String
 * @param casaEditrice: String
 * @param autore: String
 * @param annoPubblicazione: String
 * @param idCategoria: String
 * @param idGenere: String
 * @param descrizione: String
 * @param copie: ArrayList<CopiaLibro>
 * @param np: Int
 * @param image: ByteArray
 */
@Serializable
data class Libro(
    val isbn: String,
    val titolo: String,
    val sottotitolo: String?,
    val lingua: String,
    val casaEditrice: String?,
    val autore: String,
    val annoPubblicazione: String?,
    val idCategoria: Int,
    val idGenere: Int,
    val copie: ArrayList<CopiaLibro>?,
    val descrizione: String?,
    val np: Int,
    val image: String?
)