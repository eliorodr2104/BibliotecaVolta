import kotlinx.serialization.Serializable

@Serializable
data class Categoria (
    var idCategoria: Int,
    var nome: String
)