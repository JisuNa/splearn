package tobyspring.splearn.domain

class Member private constructor(
    val email: String,
    var nickname: String,
    private var passwordHash: String,
) {
    var status: MemberStatus = MemberStatus.PENDING
        private set

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

    companion object {
        fun create(email: String, nickname: String, password: String, passwordEncoder: PasswordEncoder): Member {
            return Member(email, nickname, passwordEncoder.encode(password))
        }
    }
}
