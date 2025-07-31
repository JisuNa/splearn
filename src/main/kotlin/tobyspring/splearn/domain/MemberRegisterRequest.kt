package tobyspring.splearn.domain

data class MemberRegisterRequest(
    val name: String,
    val email: Email,
    val password: String
)
