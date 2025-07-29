package tobyspring.splearn.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.dao.DataIntegrityViolationException
import tobyspring.splearn.application.required.MemberRepository
import tobyspring.splearn.domain.MemberFixture.Companion.passwordEncoder

@DataJpaTest
class MemberRepositoryTest(
    @Autowired
    private val memberRepository: MemberRepository,
    @Autowired
    private val entityManager: TestEntityManager
) {
    @Test
    fun createMember() {
        val member = Member.register(
            email = Email("toby@splearn.com"),
            nickname = "Toby",
            password = "secret",
            passwordEncoder = passwordEncoder()
        )

        memberRepository.save(member)

        assertThat(member.id).isNotNull()

        entityManager.flush()
    }

    @Test
    fun duplicateEmailFail() {
        val member = Member.register(
            email = Email("toby@splearn.com"),
            nickname = "Toby",
            password = "secret",
            passwordEncoder = passwordEncoder()
        )

        memberRepository.save(member)

        val member2 = Member.register(
            email = Email("toby@splearn.com"),
            nickname = "Toby",
            password = "secret",
            passwordEncoder = passwordEncoder()
        )

        assertThatThrownBy { memberRepository.save(member2) }
            .isInstanceOf(DataIntegrityViolationException::class.java)
    }
}
