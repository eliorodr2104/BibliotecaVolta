import kotlinx.serialization.Serializable

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