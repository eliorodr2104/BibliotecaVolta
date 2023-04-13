import java.sql.*
import java.util.*

class DBConnection {
    private var conn: Connection? = null
    private var username = "root" // provide the username
    private var password = "" // provide the corresponding password
    private val url = "jdbc:mysql://localhost/biblioteca?useSSL=false&serverTimezone=UTC"

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

    fun executeCommand(sql: String): ResultSet? {
        try {
            conn = DriverManager.getConnection(url, username, password)

            return conn?.createStatement()?.executeQuery(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            conn?.close()
        }
        return null
    }

    fun executeCommand(sql: String, statement: Statement): Statement? {
        try {
            conn = DriverManager.getConnection(url, username, password)

            return conn?.prepareStatement(sql).use { statement }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            conn?.close()
        }
        return null
    }

    fun aggiungiLibro(libro: Libro){
        try {
            conn = DriverManager.getConnection(url, username, password)
            println("Connessione al database riuscita")
            conn?.prepareStatement("INSERT INTO libri (" +
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
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").use { statement ->
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
                statement?.executeUpdate()
            }
        } catch (e: SQLException) {
            println("Connessione al database fallita")
            println(e.message)
        } finally {
            conn?.close()
        }
    }

}


/*
* @TODO generare json esito
*
*
*
*     fun aggiungiLibro(libro: Libro) {
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
