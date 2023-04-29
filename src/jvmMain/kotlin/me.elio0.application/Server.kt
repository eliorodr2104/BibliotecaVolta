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
        call.respondText(GestioneJSON().getJsonString(db.estraiTutto("Libri")))
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
    //@TODO implementare il put del libro -> C4V4H.exe
    put("/libri/{isbn}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni del libro con ISBN ${call.parameters["isbn"]} aggiornate con successo"
        )
    }
}



fun Routing.copie(db: DBConnection) {
    get("/libri/{isbn}/copie") {
        call.respond(db.estraiCopie(call.parameters["isbn"] ?: ""))
    }

    get("/libri/{isbn}/copie/{idCopia}") {
        call.respond(db.estraiCopie(call.parameters["isbn"] ?: "", call.parameters["idCopia"]))
    }

    post("/libri/{isbn}/copie") {
        try {
            val copia = call.receive<String>()
            println(db.aggiungiCopia(Json.decodeFromString(copia)))
            call.respond(HttpStatusCode.Created, "Nuovo libro creato con successo")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "ERRORE")
        }
    }

    //@TODO implementare il put della copia -> C4V4H.exe
    put("/libri/{isbn}/copie/{idCopia}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni della copia con ID ${call.parameters["idCopia"]} aggiornate con successo"
        )
    }
}



fun Routing.utenti(db: DBConnection) {
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
}



//@TODO implementare la gesione degli utenti (get, getID, post, put) -> C4V4H.exe
fun Routing.prestiti(db: DBConnection) {
    get("/utenti/{idUtente}/prestiti") {
        call.respondText("JSON contenente l'elenco di tutti i prestiti di un utente nell'ultimo anno")
    }

    get("/utenti/{idUtente}/prestiti/{idPrestito}") {
        call.respondText("JSON contenente il prestito con ID ${call.parameters["idPrestito"]} dell'utente con ID  ${call.parameters["idUtente"]}")
    }

    post("/utenti/{idUtente}/prestiti") {
        call.respond(
            HttpStatusCode.Created,
            "Nuovo prestito dell'utente con ID ${call.parameters["idUtente"]} creato con successo"
        )
    }

    put("/utenti/{idUtente}/prestiti/{idPrestito}") {
        call.respond(
            HttpStatusCode.OK,
            "Informazioni del prestito con ID ${call.parameters["idPrestito"]} dell'utente con ID ${call.parameters["idUtente"]} aggiornate con successo"
        )
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

