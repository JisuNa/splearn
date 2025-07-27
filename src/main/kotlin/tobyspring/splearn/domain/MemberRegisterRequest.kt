package tobyspring.splearn.domain

data class MemberRegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
