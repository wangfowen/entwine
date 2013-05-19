package smtp

import akka.actor.Actor
import org.apache.commons.mail.{HtmlEmail, SimpleEmail}
import models.User

class SmtpSender extends Actor {
  def receive = {
    case NewInvite(link, user) =>
      sendNewInvite(link, user)
    case GenericEmail(receipient, subject, body) =>
      sendHtmlEmail(receipient, subject, body, body)
  }

  def sendNewInvite(link: String, user: User) {
    val subject = "%s has invited you an event!".format(user.firstName + " " + user.lastName)
    val textBody = "%s has invited you to an event. Please visit the following link to choose your availability. %s".format(user.firstName + " " + user.lastName, link)
    val htmlBody = "%s has invited you to an event! Please choose your availability <a href=\"%s\">here</a>".format(user.firstName + " " + user.lastName, link)

    sendHtmlEmail(user.email, subject, textBody, htmlBody)
  }

  private def sendHtmlEmail(to: String, subject: String, textBody: String, htmlBody: String) {
    val email = new HtmlEmail()
    email.setHostName("localhost")
    email.setSmtpPort(25)
    email.setFrom("noreply@entwine.us", "Entwine")
    email.addTo(to)
    email.setSubject(subject)
    email.setTextMsg(textBody)
    email.setHtmlMsg(htmlBody)
    email.send()
  }
}
