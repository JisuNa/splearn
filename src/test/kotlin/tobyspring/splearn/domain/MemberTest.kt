package tobyspring.splearn.domain

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import kotlin.test.Test

class MemberTest {
    @Test
    fun createMember() {
        val member = Member("toby@splearn.app", "Toby", "secret")

        Assertions.assertThat(member.status.equals(MemberStatus.PENDING))
    }

    @Test
    fun activate() {
        val member = Member("toby@splearn", "Toby", "secret")

        member.activate();

        assertThat(member.status.equals(MemberStatus.ACTIVE))
    }

    @Test
    fun activateFail() {
        val member = Member("toby@splearn", "Toby", "secret")

        member.activate()

        assertThatThrownBy { member.activate() }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun deactivate() {
        val member = Member("toby@splearn", "Toby", "secret")
        member.activate()

        member.deactivate()

        assertThat(member.status.equals(MemberStatus.DEACTIVATED))
    }

    @Test
    fun deactivateFail() {
        val member = Member("toby@splearn", "Toby", "secret")

        assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)

        member.activate()
        member.deactivate()

        assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
