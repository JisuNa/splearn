package tobyspring.splearn.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.NaturalId

@Entity
class Member(
    @NaturalId
    val email: Email,
    var nickname: String,
    private var passwordHash: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.PENDING
        protected set

    fun activate() {
        check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다." }

        status = MemberStatus.ACTIVE
    }

    fun deactivate() {
        check(status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다." }

        status = MemberStatus.DEACTIVATED
    }

    fun verifyPassword(password: String, passwordEncoder: PasswordEncoder): Boolean {
        return passwordEncoder.matches(password, passwordHash)
    }

    fun changeNickname(newNickname: String) {
        this.nickname = newNickname
    }

    fun changePassword(newPassword: String, passwordEncoder: PasswordEncoder) {
        this.passwordHash = passwordEncoder.encode(newPassword)
    }

    fun isActive(): Boolean = status.isActive()

    companion object Factory {
        fun register(email: Email, nickname: String, password: String, passwordEncoder: PasswordEncoder) =
            Member(email, nickname, passwordEncoder.encode(password))

        fun register(req: MemberRegisterRequest, passwordEncoder: PasswordEncoder) = Member(
            email = req.email,
            nickname = req.nickname,
            passwordHash = passwordEncoder.encode(req.password)
        )
    }
}
