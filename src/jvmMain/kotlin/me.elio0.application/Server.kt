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

    //@TODO Stabilire come farlo andare
    put("/libri/{isbn}") {
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
            call.respond(HttpStatusCode.Created, "Nuova copia creato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    //@TODO Stabilire come farlo andare
    put("copie/{idCopia}") {
        try {
            val copia = call.receive<String>()
            println(db.aggiornaCopia(Json.decodeFromString(copia)))
            call.respond(HttpStatusCode.OK, "Copia aggiornato con successo")
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

    //@TODO Stabilire come farlo andare
    put("/utenti/{idUtente}") {
        try {
            val utente = call.receive<String>()
            println(db.aggiornaUtente(Json.decodeFromString(utente)))
            call.respond(HttpStatusCode.OK, "Copia aggiornato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "Copia non trovato")
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
            call.respond(HttpStatusCode.Created, "Nuovo utente creato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    //@TODO Stabilire come farlo andare
    put("prestiti/{idPrestito}") {
        try {
            val utente = call.receive<String>()
            println(db.aggiornaUtente(Json.decodeFromString(utente)))
            call.respond(HttpStatusCode.OK, "Copia aggiornato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "Copia non trovato")
        }
    }
}


//@TODO implementare la gesione delle categorie (get, getID, post, put) -> C4V4H.exe
fun Routing.categorie(db: DBConnection) {
    get("/categorie") {
        call.respondText("JSON contenente tutte le categorie")
    }

    get("/categorie/{idCategoria}") {
        call.respondText("JSON contenente la categoria con ID ${call.parameters["idCategoria"]}")
    }

    post("/categorie/{idCategoria}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni la categoria con ID ${call.parameters["idCategoria"]} aggiunte con successo"
        )
    }

    put("/categorie/{idCategoria}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni la categoria con ID ${call.parameters["idCategoria"]} aggiornate con successo"
        )
    }
}


//@TODO implementare la gesione dei generi (get, getID, post, put) -> C4V4H.exe
fun Routing.generi(db: DBConnection) {
    get("/generi") {
        call.respondText("JSON contenente tutti i generi")
    }

    get("/generi/{idGenere}") {
        call.respondText("JSON contenente il genere con ID ${call.parameters["idGenere"]}")
    }

    post("/generi/{idGenere}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni del genere con ID ${call.parameters["idGenere"]} aggiunte con successo"
        )
    }

    put("/generi/{idGenere}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni del genere con ID ${call.parameters["idGenere"]} aggiornate con successo"
        )
    }
}

