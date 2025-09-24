package tobyspring.splearn.adapter.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import tobyspring.splearn.domain.PasswordEncoder

@Component
class BcryptPasswordEncoder: PasswordEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    override fun encode(password: String): String {
        return bcrypt.encode(password)
    }

    override fun matches(password: String, passwordHash: String): Boolean {
        return bcrypt.matches(password, passwordHash)
    }
}