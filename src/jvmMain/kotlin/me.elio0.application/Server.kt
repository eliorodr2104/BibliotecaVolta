package me.elio0.application

import DBConnection
import GestioneJSON
import io.ktor.http.*
import io.ktor.network.tls.certificates.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.File


fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/BibliotecaVolta.js") {}
    }
}


fun main() {
    val keyStoreFile = File("keystore.jks")

    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            //domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "foobar".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
//        install(ContentNegotiation)
        module(Application::module)
    }


    //embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    biblioteca()
    install(ContentNegotiation){
        json(Json{
            prettyPrint = true
            isLenient = true
        })
    }
}


fun Application.biblioteca() {
    val db = DBConnection()
    routing {
        //libri
        get("/libri") {
            val test = db.estraiTutto("Libri")
            call.respondText(GestioneJSON().getJsonString(test))
            println(test)
            db.close()
        }
        get("/libri/{isbn}") {
            call.respond(db.estraiLibro(call.parameters["isbn"] ?: ""))
            db.close()
        }

        post("/libri") {
            val libro = call.receive<String>()
            println(db.aggiungiLibro(Json.decodeFromString(libro)))
            println(libro)
            call.respond(HttpStatusCode.Created, "Nuovo libro creato con successo")
            db.close()
        }

        put("/libri/{isbn}") {
            call.respond(
                HttpStatusCode.OK,
                "Informazioni del libro con ISBN ${call.parameters["isbn"]} aggiornate con successo"
            )
        }

        // Utenti
        get("/utenti") {
            call.respondText("JSON contenente l'elenco di tutti gli utenti")
        }

        get("/utenti/{idUtente}") {
            call.respondText("JSON contenente le informazioni dell'utente con ID ${call.parameters["idUtente"]}")
        }

        post("/utenti") {
            call.respond(HttpStatusCode.Created, "Nuovo utente creato con successo")
        }

        put("/utenti/{idUtente}") {
            call.respond(
                HttpStatusCode.OK,
                "Informazioni dell'utente con ID ${call.parameters["idUtente"]} aggiornate con successo"
            )
        }

        // Copie
        get("/libri/{isbn}/copie") {
            call.respond(db.estraiCopie(call.parameters["isbn"] ?: ""))
        }

        get("/libri/{isbn}/copie/{idCopia}") {
            call.respond(db.estraiCopie(call.parameters["isbn"] ?: "", call.parameters["idCopia"]))
        }

        post("/libri/{isbn}/copie") {
            call.respond(
                HttpStatusCode.Created,
                "Nuova copia del libro con ISBN ${call.parameters["isbn"]} creata con successo"
            )
        }

        // Prestiti
        get("/libri/{isbn}/copie/prestiti") {
            call.respondText("JSON contenente l'elenco di tutti i prestiti del libro con ISBN ${call.parameters["isbn"]}")
        }

        get("/libri/{isbn}/copie/{idCopia}/prestiti") {
            call.respondText("JSON contenente l'elenco di tutti i prestiti della copia con ID ${call.parameters["idCopia"]} del libro con ISBN ${call.parameters["isbn"]}")
        }

        post("/libri/{isbn}/copie/{idCopia}/prestiti") {
            call.respond(
                HttpStatusCode.Created,
                "Nuovo prestito della copia con ID ${call.parameters["idCopia"]} del libro con ISBN ${call.parameters["isbn"]} creato con successo"
            )
        }

        put("/libri/{isbn}/copie/{idCopia}/prestiti/{idPrestito}") {
            call.respond(
                HttpStatusCode.OK,
                "Informazioni del prestito con ID ${call.parameters["idPrestito"]} della copia con ID ${call.parameters["idCopia"]} del libro con ISBN ${call.parameters["isbn"]} aggiornate con successo"
            )
        }

        get("/libri/{isbn}/copie/{idCopia}/prestiti") {
            //val isbn = call.parameters["isbn"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            //val idCopia = call.parameters["idCopia"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            //val loans = db.getLoansByCopyId(isbn, idCopia)
            //call.respond(loans)
        }

        post("/libri/{isbn}/copie/{idCopia}/prestiti") {
            // val isbn = call.parameters["isbn"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            // val idCopia = call.parameters["idCopia"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            // val loanRequest = call.receive<LoanRequest>()
            // val loan = db.addLoan(isbn, idCopia, loanRequest.userId, loanRequest.startDate, loanRequest.endDate)
            // call.respond(loan)
        }

        put("/libri/{isbn}/copie/{idCopia}/prestiti/{idprestito}") {
            // val isbn = call.parameters["isbn"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            // val idCopia = call.parameters["idCopia"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            // val idprestito = call.parameters["idprestito"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            // val loanRequest = call.receive<LoanRequest>()
            // val updatedLoan = db.updateLoan(isbn, idCopia, idprestito, loanRequest.userId, loanRequest.startDate, loanRequest.endDate)
            // call.respond(updatedLoan)
        }
    }
}