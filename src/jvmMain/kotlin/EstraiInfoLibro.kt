import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.URL

class EstraiInfoLibro {

    fun ricercaLibro(isbn: String): Libro{
        val apiKey = "YOUR_API_KEY"
        val apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&key=$apiKey"

        val jsonString = URL(apiUrl).readText()
        val parser = JSONParser()
        val jsonObject = parser.parse(jsonString) as JSONObject
        val bookInfo = (jsonObject["items"] as ArrayList<*>)[0] as JSONObject

        val title = bookInfo["volumeInfo.title"] as String
        val authors = (bookInfo["volumeInfo.authors"] as JSONArray).joinToString(", ") { it as String }
        val language = bookInfo["volumeInfo.language"] as String
        val imageUrl = bookInfo["volumeInfo.imageLinks.thumbnail"] as String
        val publisher = bookInfo["volumeInfo.publisher"] as String?
        val publishedDate = bookInfo["volumeInfo.publishedDate"] as String?
        val pageCount = bookInfo["volumeInfo.pageCount"] as Long?
        val categories = (bookInfo["volumeInfo.categories"] as JSONArray?)?.joinToString(", ") { it as String }


        return Libro(isbn, title, language, publisher, authors,
            publishedDate, imageUrl, pageCount, categories, null)
    }

}