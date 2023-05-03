package me.elio0.application

import DBConnection
import GestioneJSON
import io.ktor.http.*
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
    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        module(Application::module)
    }

    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    biblioteca()
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}


fun Application.biblioteca() {
    val db = DBConnection()
    routing {
        libri(db)
        utenti(db)
        copie(db)
        prestiti(db)
        categorie(db)
        generi(db)
    }
}


fun Routing.libri(db: DBConnection) {
    get("/libri") {
        call.respondText(db.estraiLibri())
    }

    get("/libri/{isbn}") {
        call.respond(db.estraiLibro(call.parameters["isbn"] ?: ""))
    }

    post("/libri") {
        try {
            val libro = call.receive<String>()
            println(db.aggiungiLibro(Json.decodeFromString(libro)))
            call.respond(HttpStatusCode.Created, "Nuovo libro creato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    put("/libri") {
        try {
            val libro = call.receive<String>()
            println(db.aggiornaLibro(Json.decodeFromString(libro)))
            call.respond(HttpStatusCode.OK, "Libro aggiornato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "Libro non trovato")
        }
    }
}


fun Routing.copie(db: DBConnection) {
    get("/libri/{isbn}/copie") {
        call.respond(db.estraiCopie(call.parameters["isbn"] ?: ""))
    }

    get("copie/{idCopia}") {
        call.respond(db.estraiCopia(call.parameters["idCopia"]))
    }

    post("/libri/{isbn}/copie") {
        try {
            val copia = call.receive<String>()
            println(db.aggiungiCopia(Json.decodeFromString(copia)))
            call.respond(HttpStatusCode.Created, "Nuova copia creata con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    put("/copie") {
        try {
            val copia = call.receive<String>()
            println(db.aggiornaCopia(Json.decodeFromString(copia)))
            call.respond(HttpStatusCode.OK, "Copia aggiornata con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "Copia non trovato")
        }
    }
}


fun Routing.utenti(db: DBConnection) {
    get("/utenti") {
        call.respond(db.estraiUtenti())
    }

    get("/utenti/{idUtente}") {
        call.respond(db.estraiUtente(call.parameters["idUtente"]))
    }

    post("/utenti") {
        try {
            val utente = call.receive<String>()
            println(db.aggiungiUtente(Json.decodeFromString(utente)))
            call.respond(HttpStatusCode.Created, "Nuovo utente creato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    put("/utenti") {
        try {
            val utente = call.receive<String>()
            println(db.aggiornaUtente(Json.decodeFromString(utente)))
            call.respond(HttpStatusCode.OK, "utente aggiornato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "utente non trovato")
        }
    }
}


fun Routing.prestiti(db: DBConnection) {
    get("/utenti/{idUtente}/prestiti") {
        call.respond(db.estraiPrestiti(call.parameters["idUtente"]))
    }

    get("/prestiti/{idPrestito}") {
        call.respond(db.estraiPrestito(call.parameters["idPrestito"]))
    }

    post("/prestiti") {
        try {
            val prestito = call.receive<String>()
            println(db.aggiungiPrestito(Json.decodeFromString(prestito)))
            call.respond(HttpStatusCode.Created, "Nuovo prestito creato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    put("/prestiti") {
        try {
            val prestito = call.receive<String>()
            println(db.aggiornaPrestito(Json.decodeFromString(prestito)))
            call.respond(HttpStatusCode.OK, "prestito aggiornato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "prestito non trovato")
        }
    }
}


fun Routing.categorie(db: DBConnection) {
    get("/categorie") {
        call.respond(db.estraiCategorie())
    }

    get("/categorie/{idCategoria}") {
        call.respondText(db.estraiCategoria(call.parameters["idCategoria"]))
    }

    post("/categorie") {
        try {
            val categoria = call.receive<String>()
            println(db.aggiungiCategoria(Json.decodeFromString(categoria)))
            call.respond(HttpStatusCode.Created, "Nuova categoria creata con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    put("/categorie") {
        try {
            val categoria = call.receive<String>()
            println(db.aggiornaCategoria(Json.decodeFromString(categoria)))
            call.respond(HttpStatusCode.OK, "categoria aggiornata con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "categoria non trovata")
        }
    }
}


fun Routing.generi(db: DBConnection) {
    get("/generi") {
        call.respond(db.estraiGeneri())
    }

    get("/generi/{idGenere}") {
        call.respondText(db.estraiGenere(call.parameters["idGenere"]))
    }

    post("/generi/{idGenere}") {
        try {
            val genere = call.receive<String>()
            println(db.aggiungiGenere(Json.decodeFromString(genere)))
            call.respond(HttpStatusCode.Created, "Nuovo genere creata con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    put("/generi") {
        try {
            val genere = call.receive<String>()
            println(db.aggiornaGenere(Json.decodeFromString(genere)))
            call.respond(HttpStatusCode.OK, "genere aggiornato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "genere non trovato")
        }
    }
}

