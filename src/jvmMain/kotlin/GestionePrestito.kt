import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


/**
 * Classe per la gestione di un prestito
 * Metodi pubblici:
 *
 * - verificaPrestito(prestito : Prestito) : Long
 *
 * Da aggiungere dettagli del libro preso in prestito
 *
 * @author Ermanno Oliveri
 */
class GestionePrestito {

    //formato data DD/MM/YYYY : String
    fun verificaPrestito(prestito : Prestito) : Long{

        val b = isScaduto(prestito)
        return if (b < 0){
            //PRESTITO SCADUTO
            invioMailScaduto(prestito)
            -1
        } else {
            b
        }
    }


    /**
     * Metodo che trasforma una data in formato string (DD/MM/YYYY)
     * in un oggetto Date e lo restituisce
     *
     * @return Date data
     */
    private fun StringToDate(dateString: String?): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(dateString)
    }

    /**
     * Metodo che restituisce la data attuaòe
     */
    private fun getLocalDate() : Date {
        return Calendar.getInstance().time
    }


    /**
     * Prende un oggetto prestito, e valuta se è scaduto o meno
     * restituisce il numero di giorni rimanente alla sua scadenza,
     * o se è già avvenuta, il numero di giorni passati
     *
     * @param prestito prestito da analizzare
     * @return la differenza tra la data di fine del prestito e la data attuale
     */
    fun isScaduto(prestito : Prestito) : Long{
        val diff: Long = StringToDate(prestito.dataFine).time - getLocalDate().time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }


    /**
     * Funzione per l'invio di una mail che notifica della scadenza del
     * prestito di un libro
     *
     * @param prestito prestito per il quale inviare la comunicazione
     */
    fun invioMailScaduto(prestito : Prestito){

        // al momento configurato con mailAlternativa, con configurazione mail
        // google "volta-alessandria.it" sostituire mailAlternativa in mail
        val destinatario = prestito.persona?.mailAlternativa

        //testo della mail da rifare con le info del DB
        val testoMail = "Gentile: " + prestito.persona?.nome + " " + prestito.persona?.cognome + "\nIl tuo " +
                "prestito per il libro: " + prestito.isbn + " - copia: " + prestito.idCopia + " del " + prestito.dataInizio + " è scaduto, restituiscilo al più presto." +
                "\nBiblioteca"

        val username = "ermi1@gmx.com" //username di accesso (email)
        val password = "Tuboermi4" //password


        //da riconfigurare con autenticazione google
        val properties = Properties().apply {
            put("mail.smtp.host", "mail.gmx.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        val session = Session.getInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress("Biblioteca ITIS volta <$username>"))
            addRecipient(Message.RecipientType.TO, InternetAddress(destinatario))
            setSubject("Prestito Libro - Biblioteca")
            setText(testoMail)
        }

        Transport.send(message)

    }


}