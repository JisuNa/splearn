package tobyspring.splearn.domain

class MemberFixture {
    companion object {
        fun passwordEncoder(): PasswordEncoder = object : PasswordEncoder {
            override fun encode(password: String): String = password.uppercase()
            override fun matches(password: String, passwordHash: String): Boolean =
                password.uppercase() == passwordHash
        }
    }
}