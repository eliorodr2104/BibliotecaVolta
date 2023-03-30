import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class GestioneJSON {
    fun addBook(libro: Libro) {
        val file = File("data\\Libri.json")

        val content = (JSONParser().parse(FileReader(file)) as JSONObject)["Libri"] as JSONArray
        val nuovoLibro = JSONParser().parse(Json.encodeToString(libro)) as JSONObject


        if(controllaEsistenza(content, libro.isbn)){
            content.add(nuovoLibro)

            val fileWriter = FileWriter(file)

            val test = JSONObject()
            test["Libri"] = content

            fileWriter.write(JSONValue.toJSONString(test))
            fileWriter.close()
        }
    }

    fun controllaEsistenza(content: JSONArray, isbn: String): Boolean{
        var jsonObject: JSONObject

        for(i in 0 until content.size){
            jsonObject = content[i] as JSONObject
            if(jsonObject["isbn"] == isbn){
                return false
            }
        }
        return true
    }

    fun inizializzaLibroTest(): Libro {
        val arr: ArrayList<CopiaLibro> = ArrayList()
        arr.add(
            CopiaLibro(
                "test-prova",
                "1",
                "test",
                "0",
                Prestito(
                    true,
                    Persona(
                        "nome",
                        "cognome",
                        "nome.cognome@volta-alessandria.it",
                        "test@gmail.com",
                        "123456789"
                    ),
                    "GG/MM/YYYY",
                    "GG/MM/YYYY"
                )
            )
        )
        arr.add(
            CopiaLibro(
                "test-prova",
                "2",
                "test",
                "0",
                Prestito(
                    true,
                    Persona(
                        "nome",
                        "cognome",
                        "nome.cognome@volta-alessandria.it",
                        "test@gmail.com",
                        "123456789"
                    ),
                    "GG/MM/YYYY",
                    "GG/MM/YYYY"
                )
            )
        )

        return Libro(
            "test-prova",
            "Libro",
            "sottotitolo",
            "lingua",
            "ce",
            "autore",
            "YYYY",
            "https://google.com",
            0,
            "categoria",
            "libro di prova",
            arr
        )
    }
}