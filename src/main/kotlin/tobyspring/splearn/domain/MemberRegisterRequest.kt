package tobyspring.splearn.domain

import jakarta.validation.constraints.Size

data class MemberRegisterRequest(
    @Size(min = 5, max = 20)
    val nickname: String,
    val email: Email,
    @Size(min = 8, max = 100)
    val password: String
)
