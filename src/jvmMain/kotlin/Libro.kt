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
 * @param pathImmagine: String
 * @param nPag: Long
 * @param categoria: String
 * @param descrizione: String
 * @param copie: ArrayList<CopiaLibro>
 */
@Serializable
data class Libro (
    val isbn: String,
    val titolo: String,
    val sottotitolo: String?,
    val lingua: String,
    val casaEditrice: String?,
    val autore: String,
    val annoPubblicazione: String?,
    val pathImmagine: String,
    val nPag: Long?,
    val categoria: String?,
    val descrizione: String?,
    val copie: ArrayList<CopiaLibro>?
)