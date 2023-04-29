import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.sql.*
import kotlin.collections.ArrayList

class DBConnection {
    private var conn: Connection
    private var username = "root" // provide the username
    private var password = "" // provide the corresponding password
    private val url = "jdbc:mysql://localhost/biblioteca?useSSL=false&serverTimezone=UTC"

    init {
        conn = DriverManager.getConnection(url, username, password)
    }



    //LIBRI
    fun estraiLibri(): String {
        val arr = ArrayList<DatiLibro>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM libri")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                DatiLibro(
                    isbn = rs.getString("ISBN"),
                    titolo = rs.getString("Titolo"),
                    sottotitolo = rs.getString("Sottotitolo"),
                    lingua = rs.getString("Lingua"),
                    casaEditrice = rs.getString("CasaEditrice"),
                    autore = rs.getString("Autore"),
                    annoPubblicazione = rs.getString("AnnoPubblicazione"),
                    idCategoria = rs.getInt("IDCategoria"),
                    idGenere = rs.getInt("IDGenere"),
                    descrizione = rs.getString("Descrizione"),
                    null
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiLibro(isbn: String): String {
        try {
            val rs = estrai("SELECT * FROM Libri WHERE ISBN=$isbn")
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

    fun aggiornaLibro(libro: DatiLibro): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE libri SET " +
                    "Titolo = ?, " +
                    "Sottotitolo = ?, " +
                    "Lingua = ?, " +
                    "CasaEditrice = ?, " +
                    "Autore = ?, " +
                    "AnnoPubblicazione = ?, " +
                    "IDCategoria = ?, " +
                    "IDGenere = ?, " +
                    "Descrizione = ?, " +
                    "Immagine = ? " +
                    "WHERE ISBN = ?"
            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, libro.titolo)
            preparedStatement?.setString(2, libro.sottotitolo)
            preparedStatement?.setString(3, libro.lingua)
            preparedStatement?.setString(4, libro.casaEditrice)
            preparedStatement?.setString(5, libro.autore)
            preparedStatement?.setString(6, libro.annoPubblicazione)
            preparedStatement?.setInt(7, libro.idCategoria)
            preparedStatement?.setInt(8, libro.idGenere)
            preparedStatement?.setString(9, libro.descrizione)
            preparedStatement?.setString(10, libro.image)
            preparedStatement?.setString(11, libro.isbn)


            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Aggiornamento riuscito"
                } else {
                    "Aggiornamento fallito"
                }
            }
        } catch (e: SQLException) {
            return e.message ?: ""
        }
        return ""
    }



    //COPIE
    fun estraiCopie(isbn: String): String {
        val arr = ArrayList<CopiaLibro>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        var rs = estrai("SELECT * FROM copie WHERE isbn=$isbn")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                CopiaLibro(
                    idCopia = rs.getInt("IDCopia"),
                    isbn = rs.getString("ISBN"),
                    condizioni = rs.getString("Condizioni"),
                    inPrestito = rs.getBoolean("Prestato"),
                    sezione = rs.getString("Sezione"),
                    scaffale = rs.getInt("Scaffale"),
                    ripiano = rs.getInt("Ripiano"),
                    np = rs.getInt("NumeroPagine"),
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

    fun estraiCopia(idCopia: String?): String {
        try {
            val rs = estrai("SELECT * FROM copie WHERE IDCopia=$idCopia")
            rs.next()
            return Json.encodeToString(
                CopiaLibro(
                    idCopia = rs.getInt("IDCopia"),
                    isbn = rs.getString("ISBN"),
                    condizioni = rs.getString("Condizioni"),
                    inPrestito = rs.getBoolean("Prestato"),
                    sezione = rs.getString("Sezione"),
                    scaffale = rs.getInt("Scaffale"),
                    ripiano = rs.getInt("Ripiano"),
                    np = rs.getInt("NumeroPagine"),
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
            val query = "INSERT INTO copie (" +
                    "`IDCopia`, " +
                    "`ISBN`, " +
                    "`Condizioni`, " +
                    "`Sezione`, " +
                    "`Scaffale`, " +
                    "`Ripiano`, " +
                    "`NumeroPagine`, " +
                    "`Prestato`, " +
                    "`IDPrestito`, " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setInt(1, getNextId("IDCopia", "copie"))
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

    fun aggiornaCopia(copia: CopiaLibro): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE copie SET " +
                    "ISBN = ?, " +
                    "Condizioni = ?, " +
                    "Sezione = ?, " +
                    "Scaffale = ?, " +
                    "Ripiano = ?, " +
                    "NumeroPagine = ?, " +
                    "Prestato = ?, " +
                    "IDPrestito = ? " +
                    "WHERE IDCopia = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, copia.isbn)
            preparedStatement?.setString(2, copia.condizioni)
            preparedStatement?.setString(3, copia.sezione)
            preparedStatement?.setInt(4, copia.scaffale)
            preparedStatement?.setInt(5, copia.ripiano)
            preparedStatement?.setInt(6, copia.np)
            preparedStatement?.setBoolean(7, copia.inPrestito)
            preparedStatement?.setInt(8, copia.idPrestito)
            preparedStatement?.setInt(9, copia.idCopia)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: SQLException) {
            return e.message ?: ""
        }
        return ""
    }

    //UTENTI
    fun estraiUtenti(): String {
        val arr = ArrayList<Utente>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM utenti")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                Utente(
                    idUtente = rs.getInt("IDUtente"),
                    cognome = rs.getString("cognome"),
                    numero = rs.getString("numero"),
                    mailAlternativa = rs.getString("mailAlternativa"),
                    grado = 0,
                    nome = rs.getString("nome")
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiUtente(idUtente: String?): String {
        return try {
            val rs = estrai("SELECT * FROM utenti WHERE IDUtente=$idUtente")
            rs.next()
            Json.encodeToString(
                Utente(
                    idUtente = rs.getInt("IDUtente"),
                    cognome = rs.getString("cognome"),
                    numero = rs.getString("numero"),
                    mailAlternativa = rs.getString("mailAlternativa"),
                    grado = 0,
                    nome = rs.getString("nome")
                )
            )
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    fun aggiungiUtente(utente: Utente): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO utenti (" +
                    "`IDUtente`, " +
                    "`Mail`, " +
                    "`Nome`, " +
                    "`Cognome`, " +
                    "`Numero`, " +
                    "`MailAlternativa`, " +
                    "`GradoAccesso` " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setInt(1, getNextId("IDUtente", "utenti"))
            preparedStatement?.setString(2, utente.mail)
            preparedStatement?.setString(3, utente.nome)
            preparedStatement?.setString(4, utente.cognome)
            preparedStatement?.setString(5, utente.numero)
            preparedStatement?.setString(6, utente.mailAlternativa)
            preparedStatement?.setInt(7, utente.grado)

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

    fun aggiornaUtente(utente: Utente): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE copie SET " +
                    "Mail = ?, " +
                    "Nome = ?, " +
                    "Cognome = ?, " +
                    "Numero = ?, " +
                    "MailAlternativa = ?, " +
                    "GradoAccesso = ? " +
                    "WHERE IDUtente = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, utente.mail)
            preparedStatement?.setString(2, utente.nome)
            preparedStatement?.setString(3, utente.cognome)
            preparedStatement?.setString(4, utente.numero)
            preparedStatement?.setString(5, utente.mailAlternativa)
            preparedStatement?.setInt(6, utente.grado)
            preparedStatement?.setInt(7, utente.idUtente)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: SQLException) {
            return e.message ?: ""
        }
        return ""
    }


    //PRESTITI
    fun estraiPrestiti(idUtente: String?): String {
        val arr = ArrayList<Prestito>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM prestiti WHERE IDUtente=$idUtente")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                Prestito(
                    idPrestito = rs.getInt("IDPrestito"),
                    idCopia = rs.getInt("IDCopia"),
                    idUtente = rs.getInt("IDUtente"),
                    dataInizio = rs.getString("Inizio"),
                    dataFine = rs.getString("Fine"),
                    condizioneIniziale = rs.getString("CondizioneIniziale"),
                    condizioneFinale = rs.getString("CondizioneFinale"),
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiPrestito(idPrestito: String?): String {
        return try {
            val rs = estrai("SELECT * FROM prestiti WHERE IDPrestito=$idPrestito")
            rs.next()
            Json.encodeToString(
                Prestito(
                    idPrestito = rs.getInt("IDPrestito"),
                    idCopia = rs.getInt("IDCopia"),
                    idUtente = rs.getInt("IDUtente"),
                    dataInizio = rs.getString("Inizio"),
                    dataFine = rs.getString("Fine"),
                    condizioneIniziale = rs.getString("CondizioneIniziale"),
                    condizioneFinale = rs.getString("CondizioneFinale"),
                )
            )
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    fun aggiungiPrestito(prestito: Prestito): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO utenti (" +
                    "`IDPrestito`, " +
                    "`IDCopia`, " +
                    "`IDUtente`, " +
                    "`Inizio`, " +
                    "`Fine`, " +
                    "`CondizioneIniziale`, " +
                    "`CondizioneFinale` " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setInt(1, getNextId("IDPrestito", "prestiti"))
            preparedStatement?.setInt(2, prestito.idCopia)
            preparedStatement?.setInt(3, prestito.idUtente)
            preparedStatement?.setString(4, prestito.dataInizio)
            preparedStatement?.setString(5, prestito.dataFine)
            preparedStatement?.setString(6, prestito.condizioneIniziale)
            preparedStatement?.setString(7, prestito.condizioneFinale)

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

    fun aggiornaPretito(prestito: Prestito): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE copie SET " +
                    "IDCopia = ?, " +
                    "IDUtente = ?, " +
                    "Inizio = ?, " +
                    "Fine = ?, " +
                    "CondizioneIniziale = ?, " +
                    "CondizioneFinale = ? " +
                    "WHERE IDPrestito = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setInt(1, prestito.idCopia)
            preparedStatement?.setInt(2, prestito.idUtente)
            preparedStatement?.setString(3, prestito.dataInizio)
            preparedStatement?.setString(4, prestito.dataFine)
            preparedStatement?.setString(5, prestito.condizioneIniziale)
            preparedStatement?.setString(6, prestito.condizioneFinale)
            preparedStatement?.setInt(7, prestito.idPrestito)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: SQLException) {
            return e.message ?: ""
        }
        return ""
    }


    //UTILS
    private fun getNextId(key: String, table: String): Int {
        val result = estrai("SELECT MAX($key) FROM $table")
        result.next()
        return result.getInt(1) + 1
    }

    private fun estrai(query: String): ResultSet {
        return conn.createStatement().executeQuery(query)
    }

    fun close() {
        conn.close()
    }

}