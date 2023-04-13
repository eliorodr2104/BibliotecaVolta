import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.URL

/**
 * Estrai info libro gestisce la ricerca di un ISBn e ne estrae i dati
 * @author C4V4.exe
 */
class EstraiInfoLibro {
    /**
     * Il metodo ricercaLibro accetta un ISBN e restituisce un oggetto Libro
     * contenente le informazioni relative al libro corrispondente.
     * Se l'ISBN fornito è vuoto o se non viene trovato alcun libro,
     * viene lanciata un'eccezione corrispondente.
     * Il metodo utilizza l'API di Google Books per cercare il libro
     * e analizza la risposta JSON per estrarre le informazioni pertinenti.
     *
     * Un esempio di url che può generare è:
     * https://www.googleapis.com/books/v1/volumes?q=isbn:9788850334865&key=AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk
     *
     * @param isbn l'isbn del libro da estrarre
     * @return libro il libro trovato
     */
    fun ricercaLibro(isbn: String): Libro {
        if(isbn == ""){
            throw InvalidIsbnException("Isbn non valido")
        }
        //Api Key generata con l'account lorenzo.cavallero@volta-alessandria.it
        val apiKey = "AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk"
        //prende da Google Books l'isbn e lo carica in una stringa json
        val jsonString = URL("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&key=$apiKey").readText()
        val jsonObject = JSONParser().parse(jsonString) as JSONObject
        val items = jsonObject["items"] as? JSONArray

        if (items.isNullOrEmpty()) {
            throw BookNotFoundException("Libro non trovato")
        }

        val bookInfo = items[0] as JSONObject
        val volumeInfo = bookInfo["volumeInfo"] as JSONObject

        return Libro(
            isbn = isbn,
            titolo = volumeInfo["title"] as String,
            sottotitolo = volumeInfo["subtitle"] as String?,
            lingua = volumeInfo["language"] as String,
            casaEditrice = volumeInfo["publisher"] as String?,
            idAutore = 0/*(volumeInfo["authors"] as JSONArray?)?.joinToString(", ") { it as String } ?: ""*/,
            annoPubblicazione = volumeInfo["publishedDate"] as String?,
            idCategoria = 0/*(volumeInfo["categories"] as JSONArray?)?.joinToString(", ") { it as String }*/,
            idGenere = 0,
            descrizione = volumeInfo["description"] as String?,
            copie = null
        )
    }
}


/*
 * @param categoria: String
 * @param descrizione: String
 * @param copie: ArrayList<CopiaLibro>


AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk
esempio di ciò che dovrebbe sbucare:


{
  "kind": "books#volumes",
  "totalItems": 1,
  "items": [
    {
      "kind": "books#volume",
      "id": "HxyYwwEACAAJ",
      "etag": "aZ/zQWs11Zo",
      "selfLink": "https://www.googleapis.com/books/v1/volumes/HxyYwwEACAAJ",
      "volumeInfo": {
        "title": "Kotlin",
        "subtitle": "guida al nuovo linguaggio di Android e dello sviluppo mobile",
        "authors": [
          "Massimo Carli"
        ],
        "publishedDate": "2019",
        "industryIdentifiers": [
          {
            "type": "ISBN_10",
            "identifier": "8850334869"
          },
          {
            "type": "ISBN_13",
            "identifier": "9788850334865"
          }
        ],
        "readingModes": {
          "text": false,
          "image": false
        },
        "pageCount": 320,
        "printType": "BOOK",
        "maturityRating": "NOT_MATURE",
        "allowAnonLogging": false,
        "contentVersion": "preview-1.0.0",
        "panelizationSummary": {
          "containsEpubBubbles": false,
          "containsImageBubbles": false
        },
        "imageLinks": {
          "smallThumbnail": "http://books.google.com/books/content?id=HxyYwwEACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api",
          "thumbnail": "http://books.google.com/books/content?id=HxyYwwEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        },
        "language": "it",
        "previewLink": "http://books.google.it/books?id=HxyYwwEACAAJ&dq=isbn:9788850334865&hl=&cd=1&source=gbs_api",
        "infoLink": "http://books.google.it/books?id=HxyYwwEACAAJ&dq=isbn:9788850334865&hl=&source=gbs_api",
        "canonicalVolumeLink": "https://books.google.com/books/about/Kotlin.html?hl=&id=HxyYwwEACAAJ"
      },
      "saleInfo": {
        "country": "IT",
        "saleability": "NOT_FOR_SALE",
        "isEbook": false
      },
      "accessInfo": {
        "country": "IT",
        "viewability": "NO_PAGES",
        "embeddable": false,
        "publicDomain": false,
        "textToSpeechPermission": "ALLOWED",
        "epub": {
          "isAvailable": false
        },
        "pdf": {
          "isAvailable": false
        },
        "webReaderLink": "http://play.google.com/books/reader?id=HxyYwwEACAAJ&hl=&source=gbs_api",
        "accessViewStatus": "NONE",
        "quoteSharingAllowed": false
      }
    }
  ]
}


 */