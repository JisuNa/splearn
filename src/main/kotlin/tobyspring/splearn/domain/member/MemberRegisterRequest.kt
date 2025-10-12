package tobyspring.splearn.domain.member

import jakarta.validation.constraints.Size
import tobyspring.splearn.domain.shared.Email

data class MemberRegisterRequest(
    @Size(min = 5, max = 20)
    val nickname: String,
    val email: Email,
    @Size(min = 8, max = 100)
    val password: String
)
