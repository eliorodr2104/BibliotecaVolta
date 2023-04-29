import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.sql.*
import kotlin.collections.ArrayList

class DBConnection() {
    private var conn: Connection
    private var username = "root" // provide the username
    private var password = "" // provide the corresponding password
    private val url = "jdbc:mysql://localhost/biblioteca?useSSL=false&serverTimezone=UTC"

    init {
        conn = DriverManager.getConnection(url, username, password)
    }

    fun executeCommand(sql: String): ResultSet? {
        try {
            return conn.createStatement()?.executeQuery(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun executeCommand(sql: String, statement: Statement): Statement? {
        try {
            conn = DriverManager.getConnection(url, username, password)

            return conn.prepareStatement(sql).use { statement }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun aggiungiLibro(libro: DatiLibro): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO libri (" +
                    "`ISBN`, " + //String
                    "`Titolo`, " + //String
                    "`Sottotitolo`, " + //String
                    "`Lingua`, " + //String
                    "`CasaEditrice`, " + //String
                    "`Autore`, " + //Int
                    "`AnnoPubblicazione`, " + //String
                    "`IDCategoria`, " + //Int
                    "`IDGenere`, " + //Int
                    "`Descrizione`, " + //String
                    "`Immagine`" + //bytes
            ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, libro.isbn)
            preparedStatement?.setString(2, libro.titolo)
            preparedStatement?.setString(3, libro.sottotitolo)
            preparedStatement?.setString(4, libro.lingua)
            preparedStatement?.setString(5, libro.casaEditrice)
            preparedStatement?.setString(6, libro.autore)
            preparedStatement?.setString(7, libro.annoPubblicazione)
            preparedStatement?.setInt(8, libro.idCategoria)
            preparedStatement?.setInt(9, libro.idGenere)
            preparedStatement?.setString(10, libro.descrizione)
            preparedStatement?.setString(11, libro.image)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: SQLException) {
            return e.message ?: ""
        }
        return ""
    }


    fun estraiTutto(table: String): JSONArray {
        val arr = JSONArray()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM $table")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                JSONParser().parse(
                    Json.encodeToString(
                        DatiLibro(
                            isbn = rs.getString("ISBN"),
                            titolo = rs.getString("Titolo"),
                            sottotitolo = rs.getString("Sottotitolo"),
                            lingua = rs.getString("Lingua"),
                            casaEditrice = rs.getString("CasaEditrice"),
                            autore = rs.getString("IDAutore"),
                            annoPubblicazione = rs.getString("AnnoPubblicazione"),
                            idCategoria = rs.getInt("IDCategoria"),
                            idGenere = rs.getInt("IDGenere"),
                            descrizione = rs.getString("Descrizione"),
                            null
                        )
                    )
                ) as JSONObject

            )
        }
        // Chiude la connessione al database
        rs.close()
        return arr
    }

    private fun estrai(query: String): ResultSet {
        return conn.createStatement().executeQuery(query)
    }

    fun estraiLibro(isbn: String): String {
        try {
            val rs = estrai("SELECT * FROM Libri WHERE isbn=$isbn")
            rs.next()
            return Json.encodeToString(
                DatiLibro(
                    isbn = rs.getString("ISBN"),
                    titolo = rs.getString("Titolo"),
                    sottotitolo = rs.getString("Sottotitolo"),
                    lingua = rs.getString("Lingua"),
                    casaEditrice = rs.getString("CasaEditrice"),
                    autore = rs.getString("IDAutore"),
                    annoPubblicazione = rs.getString("AnnoPubblicazione"),
                    idCategoria = rs.getInt("IDCategoria"),
                    idGenere = rs.getInt("IDGenere"),
                    descrizione = rs.getString("Descrizione"),
                    null
                )
            )
        } catch (e: Exception) {
            return e.message ?: ""
        }
    }

    fun estraiCopie(isbn: String): String {
        val arr = ArrayList<CopiaLibro>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        var rs = estrai("SELECT * FROM copie WHERE isbn=$isbn")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                CopiaLibro(
                    isbn = rs.getString("ISBN"),
                    idCopia = rs.getString("IDCopia"),
                    condizioni = rs.getString("Condizioni"),
                    inPrestito = rs.getBoolean("Prestato"),
                    sezione = rs.getString("Sezione"),
                    scaffale = rs.getInt("Scaffale"),
                    ripiano = rs.getInt("Ripiano"),
                    np = rs.getInt("Numero Pagine"),
                    idPrestito = rs.getInt("IDPrestito")
                )
            )
        }
        rs = estrai("SELECT * FROM libri WHERE isbn=$isbn")
        rs.next()
        return Json.encodeToString(
            Libro(
                isbn = rs.getString("ISBN"),
                titolo = rs.getString("Titolo"),
                sottotitolo = rs.getString("Sottotitolo"),
                lingua = rs.getString("Lingua"),
                casaEditrice = rs.getString("CasaEditrice"),
                autore = rs.getString("IDAutore"),
                annoPubblicazione = rs.getString("AnnoPubblicazione"),
                idCategoria = rs.getInt("IDCategoria"),
                idGenere = rs.getInt("IDGenere"),
                copie = arr,
                descrizione = rs.getString("Descrizione"),
                image = null
            )
        )
    }

    fun estraiCopie(isbn: String, idCopia: String?): String {
        try {
            // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
            val rs = estrai("SELECT * FROM copie WHERE isbn=$isbn")
            rs.next()
            return Json.encodeToString(
                CopiaLibro(
                    isbn = rs.getString("ISBN"),
                    idCopia = rs.getString("IDCopia"),
                    condizioni = rs.getString("Condizioni"),
                    inPrestito = rs.getBoolean("Prestato"),
                    sezione = rs.getString("Sezione"),
                    scaffale = rs.getInt("Scaffale"),
                    ripiano = rs.getInt("Ripiano"),
                    np = rs.getInt("Numero Pagine"),
                    idPrestito = rs.getInt("IDPrestito")
                )
            )
        } catch (e: Exception) {
            return e.message ?: ""
        }
    }

    fun aggiungiCopia(copia: CopiaLibro): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO libri (" +
                    "`IDCopia`, " + //String 1
                    "`ISBN`, " + //String 2
                    "`Condizioni`, " + //String 3
                    "`Sezione`, " + //String 4
                    "`Scaffale`, " + //String 5
                    "`Ripiano`, " + //Int 6
                    "`Numero Pagine`, " + //Int 7
                    "`Prestato`, " + //Int 8
                    "`IDPrestito`, " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, copia.idCopia)
            preparedStatement?.setString(2, copia.isbn)
            preparedStatement?.setString(3, copia.condizioni)
            preparedStatement?.setString(4, copia.sezione)
            preparedStatement?.setInt(5, copia.scaffale)
            preparedStatement?.setInt(6, copia.ripiano)
            preparedStatement?.setInt(7, copia.np)
            preparedStatement?.setBoolean(8, copia.inPrestito)
            preparedStatement?.setInt(9, copia.idPrestito)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: SQLException) {
            return e.message ?: ""
        }
        return ""
    }

    //@TODO fare il coso che se un autore esiste restituisce l'id al contrario ne crea uno nuovo.
    fun analizzaAutore(autore: String): Int{
        return 0
    }

    fun close() {
        conn.close()
    }


}
/*
ISBN
Titolo
Sottotitolo
Lingua
CasaEditrice
IDAutore
AnnoPubblicazione
IDCategoria
IDGenere
Descrizione



IDCopia
ISBN
Condizioni
Sezione
Scaffale
Ripiano
Numero Pagine
Prestato
Immagine
*/


/*
* @TODO generare json esito
*
*
    fun getConnection() {
        val connectionProps = Properties()
        connectionProps["user"] = username
        connectionProps["password"] = password
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        "127.0.0.1" +
                        ":" + "3306" + "/" +
                        "",
                connectionProps
            )
        } catch (ex: SQLException) {
            // handle ay errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }
*
*
fun aggiungiLibro(libro: Libro) {
        try {
            conn = DriverManager.getConnection(url, username, password)
            println("Connessione al database riuscita")
            conn?.prepareStatement(
                "INSERT INTO libri (" +
                        "`ISBN`, " + //String
                        "`Titolo`, " + //String
                        "`Sottotitolo`, " + //String
                        "`Lingua`, " + //String
                        "`CasaEditrice`, " + //String
                        "`IDAutore`, " + //Int
                        "`AnnoPubblicazione`, " + //String
                        "`IDCategoria`, " + //Int
                        "`IDGenere`, " + //Int
                        "`Descrizione`" +
                        ")" + //String
                        " VALUES (" +
                        "${libro.isbn}, " +
                        "${libro.titolo}, " +
                        "${libro.sottotitolo}, " +
                        "${libro.lingua}, " +
                        "${libro.casaEditrice}, " +
                        "${libro.idAutore}, " +
                        "${libro.annoPubblicazione}, " +
                        "${libro.idCategoria}, " +
                        "${libro.idGenere}, " +
                        "${libro.descrizione}" +
                        ")"
            )
        } catch (e: SQLException) {
            e.printStackTrace()
            println(e.localizedMessage)
            println(e.fillInStackTrace())
            println(e.toString())
            println(e.stackTrace)
        } finally {
            conn?.close()
        }
    }
*

    fun aggiungiLibro(libro: Libro): String? {
        try {
            val rowsAffected: Int?
            conn = DriverManager.getConnection(url, username, password)
            println("Connessione al database riuscita")
            conn?.prepareStatement(
                "INSERT INTO libri (" +
                        "`ISBN`, " + //String
                        "`Titolo`, " + //String
                        "`Sottotitolo`, " + //String
                        "`Lingua`, " + //String
                        "`CasaEditrice`, " + //String
                        "`IDAutore`, " + //Int
                        "`AnnoPubblicazione`," + //Date
                        " `IDCategoria`, " + //Int
                        "`IDGenere`, " + //Int
                        "`Descrizione`) " + //String
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            ).use { statement ->
                statement?.setString(1, libro.isbn)
                statement?.setString(2, libro.titolo)
                statement?.setString(3, libro.sottotitolo)
                statement?.setString(4, libro.lingua)
                statement?.setString(5, libro.casaEditrice)
                statement?.setInt(6, libro.idAutore)
                statement?.setString(7, libro.annoPubblicazione)
                statement?.setInt(8, libro.idCategoria)
                statement?.setInt(9, libro.idGenere)
                statement?.setString(10, libro.descrizione)
                rowsAffected = statement?.executeUpdate()

            }
            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: SQLException) {
            println("Connessione al database fallita")
            return e.message
        } finally {
            conn?.close()
        }
        return ""
    }
*
*
*
  /*  fun executeMySQLQuery() {
        var stmt: Statement? = null
        var resultset: ResultSet? = null

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SHOW DATABASES;")

            if (stmt.execute("SHOW DATABASES;")) {
                resultset = stmt.resultSet
            }

            while (resultset!!.next()) {
                println(resultset.getString("Database"))
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close()
                } catch (sqlEx: SQLException) {
                }

                resultset = null
            }

            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }

                stmt = null
            }

            if (conn != null) {
                try {
                    conn!!.close()
                } catch (sqlEx: SQLException) {
                }

                conn = null
            }
        }
    }*/
    */
