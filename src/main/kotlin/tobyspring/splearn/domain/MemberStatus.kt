package tobyspring.splearn.domain

enum class MemberStatus {
    PENDING, ACTIVE, DEACTIVATED;

    fun isActive(): Boolean = this == ACTIVE
}
