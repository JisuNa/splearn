package tobyspring.splearn.domain.member

enum class MemberStatus {
    PENDING, ACTIVE, DEACTIVATED;

    fun isActive(): Boolean = this == ACTIVE
}
