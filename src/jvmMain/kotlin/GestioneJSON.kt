import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class GestioneJSON {
    suspend fun addBook(libro: Libro) {
        // Serializzare l'oggetto in formato JSON
        val jsonString = Json.encodeToString(libro)
        println(jsonString)

        // Creare un oggetto VfsFile che rappresenta il file JSON
        //val file = localVfs("test\\data\\Libri.json")

        val file = File("data\\Libri.json")

        // Scrivere la stringa JSON sul file
        val bufferedWrite = BufferedWriter(withContext(Dispatchers.IO) {
            FileWriter(file)
        })



        withContext(Dispatchers.IO) {
            bufferedWrite.append("\n$jsonString")
        }
    }
}
/*


prova creazione di un oggetto libro
val arr: ArrayList<CopiaLibro> = ArrayList()
        arr.add(
            CopiaLibro(
                "123-4567-890",
                "1",
                "nuovo",
                "C7",
                Prestito(
                    true,
                    Persona(
                        "Pippo",
                        "pluto",
                        " ",
                        "test",
                        "3447268745"
                    ),
                    "12/12/2012",
                    "12/12/2012"
                )
            )
        )
        arr.add(
            CopiaLibro(
                "123-4567-890",
                "2",
                "come nuovo",
                "C9",
                Prestito(
                    true,
                    Persona(
                        "Paperino",
                        "test",
                        " ",
                        "test",
                        "3447268745346235468"
                    ),
                    "12/12/2012",
                    "12/12/2012"
                )
            )
        )

        val book = Libro(
            "123-4567-890", "Libro",
            "Eng",
            "roberto",
            "io",
            "2022",
            "src\\ciao.png",
            10,
            "informatica",
            arr
        )*/
