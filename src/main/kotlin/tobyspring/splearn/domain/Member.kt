package tobyspring.splearn.domain

data class Member(
    private val email: String,
    private var nickname: String,
    private val passwordHash: String,
) {
    var status: MemberStatus = MemberStatus.PENDING

    fun activate() {
        check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다." }

        status = MemberStatus.ACTIVE
    }

    fun deactivate() {
        check(status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다." }

        status = MemberStatus.DEACTIVATED
    }
}
