package tobyspring.splearn.domain.member

import jakarta.persistence.Entity
import org.springframework.util.Assert
import tobyspring.splearn.domain.AbstractEntity
import java.time.LocalDateTime

@Entity
class MemberDetail(
    var profile: String? = null,
    var introduction: String? = null,
    val registeredAt: LocalDateTime,
    var activatedAt: LocalDateTime? = null,
    var deactivatedAt: LocalDateTime? = null
) : AbstractEntity() {
    fun activate() {
        Assert.isTrue(activatedAt == null, "이미 ActivatedAt은 설정되었습니다.")

        this.activatedAt = LocalDateTime.now()
    }

    fun deactivate() {
        Assert.isTrue(activatedAt == null, "이미 DeactivatedAt은 설정되었습니다.")

        this.deactivatedAt = LocalDateTime.now()
    }

    companion object Factory {
        fun create(): MemberDetail {
            return MemberDetail(registeredAt = LocalDateTime.now())
        }
    }
}
