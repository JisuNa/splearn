package tobyspring.splearn.domain

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue

class MemberTest {
    lateinit var member: Member
    lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setUp() {
        this.passwordEncoder = object : PasswordEncoder {
            override fun encode(password: String): String = password.uppercase()
            override fun matches(password: String, passwordHash: String): Boolean =
                password.uppercase() == passwordHash
        }

        member = Member.create(
            email = Email("toby@splearn.com"),
            nickname = "Toby",
            password = "secret",
            passwordEncoder = passwordEncoder
        )
    }

    @Test
    fun createMember() {
        Assertions.assertThat(member.status.equals(MemberStatus.PENDING))
    }

    @Test
    fun activate() {
        member.activate();

        assertThat(member.status.equals(MemberStatus.ACTIVE))
    }

    @Test
    fun activateFail() {
        member.activate()

        assertThatThrownBy { member.activate() }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun deactivate() {
        member.activate()

        member.deactivate()

        assertThat(member.status == MemberStatus.DEACTIVATED)
    }

    @Test
    fun deactivateFail() {
        assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)

        member.activate()
        member.deactivate()

        assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun verifyPassword() {
        assertTrue(member.verifyPassword(password = "secret", passwordEncoder = passwordEncoder))
        assertFalse(member.verifyPassword(password = "hello", passwordEncoder = passwordEncoder))
    }

    @Test
    fun changeNickname() {
        assertThat(member.nickname == "Toby")

        member.changeNickname(newNickname = "charlie")

        assertThat(member.nickname == "charlie")
    }

    @Test
    fun changePassword() {
        member.changePassword(newPassword = "newSecret", passwordEncoder = passwordEncoder)

        assertTrue(member.verifyPassword(password = "newSecret", passwordEncoder = passwordEncoder))
    }

    @Test
    fun isActive() {
        assertThat(member.isActive()).isFalse

        member.activate()

        assertThat(member.isActive()).isTrue

        member.deactivate()

        assertThat(member.isActive()).isFalse
    }

    @Test
    fun invalidEmail() {
        assertThatThrownBy {
            Member.create(
                email = Email("invalid email"),
                nickname = "Toby",
                password = "secret",
                passwordEncoder = passwordEncoder
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
