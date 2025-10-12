package tobyspring.splearn.domain.member

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import tobyspring.splearn.domain.AbstractEntity
import tobyspring.splearn.domain.PasswordEncoder
import tobyspring.splearn.domain.shared.Email

@Entity
class Member(
    val email: Email,

    var nickname: String,

    private var passwordHash: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var detail: MemberDetail? = null

) : AbstractEntity() {
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    var status: MemberStatus = MemberStatus.PENDING
        protected set

    fun activate() {
        check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다." }

        status = MemberStatus.ACTIVE

        detail!!.activate()
    }

    fun deactivate() {
        check(status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다." }

        status = MemberStatus.DEACTIVATED

        detail!!.deactivate()
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

        fun register(req: MemberRegisterRequest, passwordEncoder: PasswordEncoder): Member {
            return Member(
                email = req.email,
                nickname = req.nickname,
                passwordHash = passwordEncoder.encode(req.password),
                detail = MemberDetail.create()
            )
        }
    }
}
