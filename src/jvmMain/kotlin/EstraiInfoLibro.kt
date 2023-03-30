import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.URL

class EstraiInfoLibro {
    //https://www.googleapis.com/books/v1/volumes?q=isbn:9788850334865&key=AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk
    fun ricercaLibro(isbn: String): Libro {
        if(isbn == ""){
            throw InvalidIsbnException("Isbn non valido")
        }
        val apiKey = "AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk"
        val jsonString = URL("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&key=$apiKey").readText()
        val parser = JSONParser()
        val jsonObject = parser.parse(jsonString) as JSONObject
        val items = jsonObject["items"] as? JSONArray

        if (items.isNullOrEmpty()) {
            throw BookNotFoundException("Libro non trovato")
        }

        val bookInfo = items[0] as JSONObject
        val volumeInfo = bookInfo["volumeInfo"] as JSONObject

        val title = volumeInfo["title"] as String
        val subtitle = volumeInfo["subtitle"] as String?
        val authors = (volumeInfo["authors"] as JSONArray?)?.joinToString(", ") { it as String } ?: ""
        val language = volumeInfo["language"] as String
        val imageUrl = (volumeInfo["imageLinks"] as JSONObject?)?.get("thumbnail") as String? ?: ""
        val publisher = volumeInfo["publisher"] as String?
        val publishedDate = volumeInfo["publishedDate"] as String?
        val pageCount = volumeInfo["pageCount"] as Long?
        val categories = (volumeInfo["categories"] as JSONArray?)?.joinToString(", ") { it as String }

        val description = volumeInfo["description"] as String?

        return Libro(
            isbn, title, subtitle, language, publisher, authors,
            publishedDate, imageUrl, pageCount, categories, description, null
        )
    }

}


/*
esempio di ciò che dovrebbe sbucare:

val isbn: String,
val titolo: String,
val sottotitolo: String,
val lingua: String,
val casaEditrice: String?,
val autore: String,
val annoPubblicazione: String?,
val pathImmagine: String,
val nPag: Long?,
val categoria: String?,
val descrizione: String?,
val copie: ArrayList<CopiaLibro>?



AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk
AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk
AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk
AIzaSyC_jEjuIHolKlAfkWUd35SCEgEyFtS2JGk




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