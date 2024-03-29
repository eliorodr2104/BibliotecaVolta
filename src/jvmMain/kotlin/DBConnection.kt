import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.IllegalArgumentException
import java.sql.*
import kotlin.collections.ArrayList

class DBConnection {
    private var conn: Connection
    private var username = "admin" // provide the username
    private var password = "Biblioteca-DATABASE-2023" // provide the corresponding password
    private val url = "jdbc:mysql://database-biblioteca.c4zsxfczq0qj.us-east-1.rds.amazonaws.com:3306/biblioteca?characterEncoding=UTF-8"

    //ALTER TABLE tablename AUTO_INCREMENT = 1

    init {
        conn = DriverManager.getConnection(url, username, password)
    }


    //LIBRI
    fun estraiLibri(page: Int): String {
        try {
            val arr = ArrayList<DatiLibro>(10)
            // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
            val rs = estrai("SELECT * FROM libri ORDER BY Titolo LIMIT 10 OFFSET ${page * 10}")
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
                        idCategorie = rs.getString("IDCategorie"),
                        idGenere = rs.getInt("IDGenere"),
                        descrizione = rs.getString("Descrizione"),
                        np = rs.getInt("NumeroPagine"),
                        image = rs.getString("Immagine")
                    )
                )
            }
            return Json.encodeToString(arr)
        } catch (e: Exception) {
            return e.message ?: ""
        }
    }

    fun estraiLibri(filtro: Map<*, *>?, page: Int): String {
        try {
            val arr = ArrayList<DatiLibro>()
            var query = "SELECT * FROM libri WHERE "

            for (entry in filtro!!.entries.iterator()) {
                query += "${entry.key} LIKE '%${entry.value}%' COLLATE latin1_general_ci AND "
            }
            query = query.dropLast(5)

            // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
            val rs = estrai(query + " ORDER BY Titolo LIMIT 10 OFFSET ${page * 10}")
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
                        idCategorie = rs.getString("IDCategorie"),
                        idGenere = rs.getInt("IDGenere"),
                        descrizione = rs.getString("Descrizione"),
                        np = rs.getInt("NumeroPagine"),
                        image = rs.getString("Immagine")
                    )
                )
            }
            return Json.encodeToString(arr)
        } catch (e: Exception) {
            return e.message ?: ""
        }
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
                    autore = rs.getString("Autore"),
                    annoPubblicazione = rs.getString("AnnoPubblicazione"),
                    idCategorie = rs.getString("IDCategorie"),
                    idGenere = rs.getInt("IDGenere"),
                    descrizione = rs.getString("Descrizione"),
                    np = rs.getInt("NumeroPagine"),
                    image = rs.getString("Immagine")
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
                    "`IDCategorie`, " + //Int
                    "`IDGenere`, " + //Int
                    "`Descrizione`, " + //String
                    "`NumeroPagine`, " + //String
                    "`Immagine`" + //bytes
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, libro.isbn)
            preparedStatement?.setString(2, libro.titolo)
            preparedStatement?.setString(3, libro.sottotitolo)
            preparedStatement?.setString(4, libro.lingua)
            preparedStatement?.setString(5, libro.casaEditrice)
            preparedStatement?.setString(6, libro.autore)
            preparedStatement?.setString(7, libro.annoPubblicazione)
            preparedStatement?.setString(8, verificaPKfromList(libro.idCategorie))
            preparedStatement?.setInt(9, verificaPK(libro.idGenere, "generi", "IDGenere"))
            preparedStatement?.setString(10, libro.descrizione)
            preparedStatement?.setInt(11, libro.np)
            preparedStatement?.setString(12, libro.image)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: Exception) {
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
                    "IDCategorie = ?, " +
                    "IDGenere = ?, " +
                    "Descrizione = ?, " +
                    "NumeroPagine = ?, " +
                    "Immagine = ? " +
                    "WHERE ISBN = ?"
            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, libro.titolo)
            preparedStatement?.setString(2, libro.sottotitolo)
            preparedStatement?.setString(3, libro.lingua)
            preparedStatement?.setString(4, libro.casaEditrice)
            preparedStatement?.setString(5, libro.autore)
            preparedStatement?.setString(6, libro.annoPubblicazione)
            preparedStatement?.setString(7, verificaPKfromList(libro.idCategorie))
            preparedStatement?.setInt(8, verificaPK(libro.idGenere, "generi", "IDGenere"))
            preparedStatement?.setString(9, libro.descrizione)
            preparedStatement?.setInt(10, libro.np)
            preparedStatement?.setString(11, libro.image)
            preparedStatement?.setString(12, libro.isbn)


            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Aggiornamento riuscito"
                } else {
                    "Aggiornamento fallito"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }


    //COPIE
    fun estraiCopie(isbn: String): String {
        val arr = ArrayList<CopiaLibro>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        var rs = estrai("SELECT * FROM copie WHERE isbn=$isbn AND Attiva=1")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                CopiaLibro(
                    idCopia = rs.getInt("IDCopia"),
                    isbn = rs.getString("ISBN"),
                    condizioni = rs.getString("Condizioni"),
                    sezione = rs.getString("Sezione"),
                    scaffale = rs.getInt("Scaffale"),
                    ripiano = rs.getInt("Ripiano"),
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
                autore = rs.getString("Autore"),
                annoPubblicazione = rs.getString("AnnoPubblicazione"),
                idCategorie = rs.getString("IDCategorie"),
                idGenere = rs.getInt("IDGenere"),
                copie = arr,
                descrizione = rs.getString("Descrizione"),
                np = rs.getInt("NumeroPagine"),
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
                    sezione = rs.getString("Sezione"),
                    scaffale = rs.getInt("Scaffale"),
                    ripiano = rs.getInt("Ripiano"),
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
                    "`ISBN`, " +
                    "`Condizioni`, " +
                    "`Sezione`, " +
                    "`Scaffale`, " +
                    "`Ripiano`, " +
                    "`IDPrestito` " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, copia.isbn)
            preparedStatement?.setString(2, copia.condizioni)
            preparedStatement?.setString(3, copia.sezione)
            preparedStatement?.setInt(4, copia.scaffale ?: 0)
            preparedStatement?.setInt(5, copia.ripiano ?: 0)
            preparedStatement?.setInt(6, verificaPK(copia.idPrestito, "prestiti", "IDPrestito"))

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: Exception) {
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
                    "IDPrestito = ? " +
                    "WHERE IDCopia = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, copia.isbn)
            preparedStatement?.setString(2, copia.condizioni)
            preparedStatement?.setString(3, copia.sezione)
            preparedStatement?.setInt(4, copia.scaffale ?: 0)
            preparedStatement?.setInt(5, copia.ripiano ?: 0)
            preparedStatement?.setInt(6, verificaPK(copia.idPrestito, "prestiti", "IDPrestito"))
            preparedStatement?.setInt(7, copia.idCopia)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }

    fun deleteCopia(pk: String): String {
        return try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE copie SET Attiva = 0 WHERE IDCopia = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, pk)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected!! > 0)
                "Copia eliminata"
            else
                "Nessuna copia trovata"

        } catch (e: Exception) {
            e.message ?: "errore"
        }
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
                    nome = rs.getString("nome"),
                    cognome = rs.getString("cognome"),
                    numero = rs.getString("numero"),
                    mailAlternativa = rs.getString("mailAlternativa"),
                    grado = rs.getInt("GradoAccesso"),
                    preferiti = rs.getString("Preferiti"),
                    mail = rs.getString("Mail")
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiUtente(mail: String): String {
        return try {
            val rs = estrai("SELECT * FROM utenti WHERE Mail='$mail'")
            rs.next()
            Json.encodeToString(
                Utente(
                    idUtente = rs.getInt("IDUtente"),
                    nome = rs.getString("nome"),
                    cognome = rs.getString("cognome"),
                    numero = rs.getString("numero"),
                    mailAlternativa = rs.getString("mailAlternativa"),
                    grado = rs.getInt("GradoAccesso"),
                    preferiti = rs.getString("Preferiti"),
                    mail = rs.getString("Mail")
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
                    "`Mail`, " +
                    "`Nome`, " +
                    "`Cognome`, " +
                    "`Numero`, " +
                    "`MailAlternativa`, " +
                    "`GradoAccesso`, " +
                    "`Preferiti`" +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, utente.mail)
            preparedStatement?.setString(2, utente.nome)
            preparedStatement?.setString(3, utente.cognome)
            preparedStatement?.setString(4, utente.numero)
            preparedStatement?.setString(5, utente.mailAlternativa)
            preparedStatement?.setInt(6, utente.grado)
            preparedStatement?.setString(7, utente.preferiti)


            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }

    fun aggiornaUtente(utente: Utente): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE utenti SET " +
                    "Mail = ?, " +
                    "Nome = ?, " +
                    "Cognome = ?, " +
                    "Numero = ?, " +
                    "MailAlternativa = ?, " +
                    "GradoAccesso = ?, " +
                    "Preferiti = ?" +
                    "WHERE IDUtente = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, utente.mail)
            preparedStatement?.setString(2, utente.nome)
            preparedStatement?.setString(3, utente.cognome)
            preparedStatement?.setString(4, utente.numero)
            preparedStatement?.setString(5, utente.mailAlternativa)
            preparedStatement?.setInt(6, utente.grado)
            preparedStatement?.setString(7, utente.preferiti)
            preparedStatement?.setInt(8, utente.idUtente)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: Exception) {
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
                    dataFine = rs.getString("Fine"),
                    dataInizio = rs.getString("Inizio"),
                    condizioneIniziale = rs.getString("CondizioneIniziale"),
                    condizioneFinale = rs.getString("CondizioneFinale"),
                    attivo = true
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiTuttiPrestiti(): ArrayList<Prestito> {
        val arr = ArrayList<Prestito>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM prestiti WHERE Attivo=1")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                Prestito(
                    idPrestito = rs.getInt("IDPrestito"),
                    idCopia = rs.getInt("IDCopia"),
                    idUtente = rs.getInt("IDUtente"),
                    dataFine = rs.getString("Fine"),
                    dataInizio = rs.getString("Inizio"),
                    condizioneIniziale = rs.getString("CondizioneIniziale"),
                    condizioneFinale = rs.getString("CondizioneFinale"),
                    attivo = rs.getBoolean("Attivo")
                )
            )
        }
        return arr
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
                    dataFine = rs.getString("Fine"),
                    dataInizio = rs.getString("Inizio"),
                    condizioneIniziale = rs.getString("CondizioneIniziale"),
                    condizioneFinale = rs.getString("CondizioneFinale"),
                    attivo = rs.getBoolean("Attivo")
                )
            )
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    fun aggiungiPrestito(prestito: Prestito): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO prestiti (" +
                    "`IDCopia`, " +
                    "`IDUtente`, " +
                    "`Inizio`, " +
                    "`Fine`, " +
                    "`CondizioneIniziale`, " +
                    "`CondizioneFinale` " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setInt(1, verificaPK(prestito.idCopia, "copie", "IDCopia"))
            preparedStatement?.setInt(2, verificaPK(prestito.idUtente, "utenti", "IDUtente"))
            preparedStatement?.setString(3, prestito.dataInizio)
            preparedStatement?.setString(4, prestito.dataFine)
            preparedStatement?.setString(5, prestito.condizioneIniziale)
            preparedStatement?.setString(6, prestito.condizioneFinale)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
            togglePrestitoCopia(prestito, true)
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }

    fun aggiornaPrestito(prestito: Prestito): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE prestiti SET " +
                    "IDCopia = ?, " +
                    "IDUtente = ?, " +
                    "Inizio = ?, " +
                    "Fine = ?, " +
                    "CondizioneIniziale = ?, " +
                    "CondizioneFinale = ? " +
                    "WHERE IDPrestito = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setInt(1, verificaPK(prestito.idCopia, "copie", "IDCopia"))
            preparedStatement?.setInt(2, verificaPK(prestito.idUtente, "utenti", "IDUtente"))
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
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }


    //categorie
    fun estraiCategorie(): String {
        val arr = ArrayList<Categoria>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM categorie")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                Categoria(
                    idCategoria = rs.getInt("IDCategoria"),
                    nome = rs.getString("NomeCategoria")
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiCategorie(str: String): String {
        val arr: List<String>
        val ris = ArrayList<String>()
        return try {
            arr = str.split(",")
            for (i in arr.indices) {
                ris.add(
                    estraiCategoria(arr[i])
                )
            }
            Json.encodeToString(ris)
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    private fun estraiCategoria(idCategoria: String?): String {
        val rs = estrai("SELECT NomeCategoria FROM categorie WHERE IDCategoria=$idCategoria")
        rs.next()
        return rs.getString("NomeCategoria")
    }

    fun aggiungiCategoria(categoria: Categoria): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO categorie (" +
                    "`NomeCategoria` " +
                    ") " +
                    "VALUES (?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, categoria.nome)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }

    fun aggiornaCategoria(categoria: Categoria): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE categorie SET " +
                    "NomeCategoria = ? " +
                    "WHERE IDCategoria = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, categoria.nome)
            preparedStatement?.setInt(2, categoria.idCategoria)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }


    //GENERI
    fun estraiGeneri(): String {
        val arr = ArrayList<Genere>()
        // Crea la connessione al database ed esegue il comando "SELECT * FROM $table" che estrae tutti gli elementi della tabella data
        val rs = estrai("SELECT * FROM generi")
        //aggiunge tutti gli elementi trovati a un arraylist
        while (rs.next()) {
            arr.add(
                Genere(
                    idGenere = rs.getInt("IDgenere"),
                    nome = rs.getString("NomeGenere")
                )
            )
        }
        return Json.encodeToString(arr)
    }

    fun estraiGenere(idGenere: String?): String {
        return try {
            val rs = estrai("SELECT * FROM genere WHERE IDGenere=$idGenere")
            rs.next()
            Json.encodeToString(
                Genere(
                    idGenere = rs.getInt("IDGenere"),
                    nome = rs.getString("NomeGenere")
                )
            )
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    fun aggiungiGenere(genere: Genere): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "INSERT INTO generi (" +
                    "`NomeGenere` " +
                    ") " +
                    "VALUES (?)"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, genere.nome)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Inserimento riuscito"
                } else {
                    "Inserimento fallito"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }

    fun aggiornaGenere(genere: Genere): String {
        try {
            val preparedStatement: PreparedStatement?
            val query = "UPDATE generi SET " +
                    "NomeGenere = ? " +
                    "WHERE IDgenere = ?"

            preparedStatement = conn.prepareStatement(query)
            preparedStatement?.setString(1, genere.nome)
            preparedStatement?.setInt(2, genere.idGenere)

            val rowsAffected = preparedStatement?.executeUpdate()

            if (rowsAffected != null) {
                return if (rowsAffected > 0) {
                    "Modifica riuscita"
                } else {
                    "Nessuna copia modificata"
                }
            }
        } catch (e: Exception) {
            return e.message ?: ""
        }
        return ""
    }

    //UTILS
    private fun estrai(query: String): ResultSet {
        return conn.createStatement().executeQuery(query)
    }

    fun togglePrestitoCopia(prestito: Prestito, bool: Boolean) {
        val copia =
            GestioneJSON().getCopiaFromString(estraiCopia(verificaPK(prestito.idCopia, "copie", "IDCopia").toString()))
        prestito.attivo = false
        aggiornaPrestito(prestito)
        if (bool)
            copia.idPrestito = getMax()
        else
            copia.idPrestito = -1
        aggiornaCopia(copia)
    }

    private fun verificaPK(id: Int, table: String, nomePK: String): Int {
        val sql = "SELECT $nomePK FROM $table WHERE $nomePK = ?"
        val preparedStatement = conn.prepareStatement(sql)
        preparedStatement.setInt(1, id)
        if (!preparedStatement.executeQuery().next() && id != -1)
            throw IllegalArgumentException("\"$nomePK\" con valore \"$id\" assente nella tabella \"$table\"")
        return id
    }

    private fun verificaPKfromList(list: String): String {
        val id = list.split(",")
        for (i in id.indices) {
            val preparedStatement = conn.prepareStatement("SELECT IDCategoria FROM categorie WHERE IDCategoria = ?")
            preparedStatement.setString(1, id[i])

            if (!preparedStatement.executeQuery().next() && id[i].toInt() != -1)
                throw IllegalArgumentException("\"IDCategorie\" con valore \"${id[i]}\" assente nella tabella \"Categorie\"")
        }
        return id.joinToString(",")
    }

    private fun getMax(): Int {
        val rs = estrai("SELECT MAX(IDPrestito) FROM prestiti")
        if (rs.next()) {
            return rs.getInt("IDPrestito")
        }
        return 1
    }
}