import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import com.google.gson.Gson


/**
 * @author C4V4H.exe
 *
 * classe Gestione JSON che permette di aggiungere un oggetto dentro un json
 */
class GestioneJSON {
    /**
     *Il metodo addBook aggiunge un oggetto Libro al file Libri.json.
     * Utilizza la libreria JSONParser per analizzare il contenuto del file
     * e Json.encodeToString per trasformare l'oggetto Libro in un oggetto JSON.
     * Il nuovo libro viene aggiunto all'array JSON solo se non esiste già nel file.
     *
     * @param libro Libro da aggiungere
     */
    fun addBook(libro: Libro, path: String) {
        val file = File(path)

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

    /**
     * Controlla se esiste già un isbn in un JSONArray
     *
     * @param content Json array da verificare
     * @param isbn isbn da cercare
     * @return Boolean esiste o no
     */
    private fun controllaEsistenza(content: JSONArray, isbn: String): Boolean{
        var jsonObject: JSONObject

        for(i in 0 until content.size){
            jsonObject = content[i] as JSONObject
            if(jsonObject["isbn"] == isbn){
                return false
            }
        }
        return true
    }

    fun getJsonString(list: JSONArray): String{
        val obj= JSONObject()
        obj["array"] = list
        return obj.toJSONString()
    }

    fun getJsonString(any: DatiLibro): String{
        return Json.encodeToString(any)
    }

    fun getCopiaFromString(copia: String):CopiaLibro{
        return Gson().fromJson(copia, CopiaLibro::class.java)
    }
}
    /*
     * Metodo per fare test, che crea un oggetto libro completo e lo returna
     *
     * @return libro di test

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
                        "123456789",
                        "test@gmail.com",
                        "nome.cognome@volta-alessandria.it"
                    ),
                    "GG/MM/YYYY",
                    "GG/MM/YYYY",
                    "1",
                    "test-prova"
                ),
                false
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
                        "123456789",
                        "test@gmail.com"
                    ),
                    "GG/MM/YYYY",
                    "GG/MM/YYYY",
                    "2",
                    "test-prova"
                ),
                false
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
    }     */



/*
Esempio di formato json che si ottiene con la classe:
(null = non trovato o assente)

{
  "Libri": [
    {
      "descrizione": null,
      "titolo": "Kotlin",
      "annoPubblicazione": "2019",
      "sottotitolo": "guida al nuovo linguaggio di Android e dello sviluppo mobile",
      "lingua": "it",
      "casaEditrice": null,
      "isbn": "9788850334865",
      "categoria": null,
      "copie": null,
      "nPag": 320,
      "autore": "Massimo Carli",
      "pathImmagine": "http:\/\/books.google.com\/books\/content?id=HxyYwwEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
    },
    {
      "descrizione": null,
      "titolo": "Naila di Mondo9",
      "annoPubblicazione": "2018",
      "sottotitolo": null,
      "lingua": "it",
      "casaEditrice": "Oscar fantastica",
      "isbn": "9788804688259",
      "categoria": "Fiction",
      "copie": null,
      "nPag": 310,
      "autore": "Dario Tonani",
      "pathImmagine": "http:\/\/books.google.com\/books\/content?id=2UUQvAEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
    }
  ]
}*/