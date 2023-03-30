import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.math.abs


/**
 * Classe per la gestione di un prestito
 * Metodi pubblici:
 *
 * - verificaPrestito(prestito : Prestito) : Long
 *
 * Da aggiungere dettagli del libro preso in prestito
 */
class GestionePrestito {

    //formato data DD/MM/YYYY : String
    fun verificaPrestito(prestito : Prestito) : Long{

        val b = isScaduto(prestito)
        if (b < 0){
            //PRESTITO SCADUTO
            invioMailScaduto(prestito);
            return -1;
        } else {
            return b
        }
    }

    fun StringToDate(dateString: String?): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(dateString)
    }

    fun getLocalDate() : Date {
        return Calendar.getInstance().time
    }

    fun isScaduto(prestito : Prestito) : Long{
        val diff: Long = StringToDate(prestito.dataFine).time - getLocalDate().time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun invioMailScaduto(prestito : Prestito){

        val destinatario = prestito.persona?.mailAlternativa
        var testoMail = "Gentile: " + prestito.persona?.nome + " " + prestito.persona?.cognome + "\nIl tuo " +
                "prestito per il libro: " + prestito.isbn + " - copia: " + prestito.idCopia + " del " + prestito.dataInizio + " è scaduto, restituiscilo al più presto." +
                "\nBiblioteca"



        val username = "ermi1@gmx.com"
        val password = "Tuboermi4"


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