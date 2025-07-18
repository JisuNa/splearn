package tobyspring.splearn.domain

data class MemberCreateRequest(
    val name: String,
    val email: String,
    val password: String
)
