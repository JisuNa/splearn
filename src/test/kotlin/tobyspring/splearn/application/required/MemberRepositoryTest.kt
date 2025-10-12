package tobyspring.splearn.application.required

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.dao.DataIntegrityViolationException
import tobyspring.splearn.application.member.required.MemberRepository
import tobyspring.splearn.domain.member.Member
import tobyspring.splearn.domain.member.MemberFixture.Companion.passwordEncoder
import tobyspring.splearn.domain.member.MemberStatus
import tobyspring.splearn.domain.shared.Email

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
        entityManager.clear()

        val savedMember = memberRepository.findById(member.id)!!

        assertThat(savedMember.status).isEqualTo(MemberStatus.PENDING)
        assertThat(savedMember.detail!!.activatedAt).isNotNull
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