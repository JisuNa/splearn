package tobyspring.splearn.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Member(
    val email: Email,
    var nickname: String,
    private var passwordHash: String,
): AbstractEntity() {
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
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
