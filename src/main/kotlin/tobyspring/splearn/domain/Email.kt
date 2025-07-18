package tobyspring.splearn.domain

import java.util.regex.Pattern

@JvmInline
value class Email(val email: String) {
    init {
        check(EMAIL_PATTERN.matcher(email).matches()) {
            "Invalid email: $email"
        }
    }

    companion object Constants {
        private val EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    }
}
