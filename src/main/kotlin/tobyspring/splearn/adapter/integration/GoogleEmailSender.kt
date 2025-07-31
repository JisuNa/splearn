package tobyspring.splearn.adapter.integration

import org.springframework.stereotype.Component
import tobyspring.splearn.application.required.EmailSender
import tobyspring.splearn.domain.Email

@Component
class GoogleEmailSender: EmailSender {
    override fun send(email: Email, subject: String, body: String) {
        println("Sending email $email to $subject")
    }
}