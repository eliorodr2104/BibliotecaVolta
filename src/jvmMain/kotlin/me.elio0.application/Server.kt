package me.elio0.application

import DBConnection
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}


fun Application.module() {
    biblioteca()
}

fun Application.biblioteca() {
    val db = DBConnection()
    routing {
        //libri
        get("/libri") {
            call.respondText("JSON contenente l'elenco di tutti i libri")
        }
        get("/libri/{isbn}") {
            call.respondText("JSON contenente le informazioni del libro con ISBN ${call.parameters["isbn"]}")
        }

        post("/libri") {
            val libro = call.receive<Json>()
            println(db.aggiungiLibro(Json.decodeFromString(libro.toString())))
            println(libro)
            call.respond(HttpStatusCode.Created, "Nuovo libro creato con successo")
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
            call.respondText("JSON contenente l'elenco di tutte le copie del libro con ISBN ${call.parameters["isbn"]}")
        }

        get("/libri/{isbn}/copie/{idCopia}") {
            call.respondText("JSON contenente le informazioni della copia con ID ${call.parameters["idCopia"]} del libro con ISBN ${call.parameters["isbn"]}")
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