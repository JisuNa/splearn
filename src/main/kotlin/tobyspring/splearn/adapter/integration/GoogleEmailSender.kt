package tobyspring.splearn.adapter.integration

import org.springframework.context.annotation.Fallback
import org.springframework.stereotype.Component
import tobyspring.splearn.application.member.required.EmailSender
import tobyspring.splearn.domain.shared.Email

@Component
@Fallback
class GoogleEmailSender: EmailSender {
    override fun send(email: Email, subject: String, body: String) {
        println("Sending email $email to $subject")
    }
}
