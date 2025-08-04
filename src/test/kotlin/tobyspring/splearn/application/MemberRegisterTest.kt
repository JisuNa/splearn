package tobyspring.splearn.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import tobyspring.splearn.application.required.EmailSender
import tobyspring.splearn.application.required.MemberRepository
import tobyspring.splearn.domain.DuplicateEmailException
import tobyspring.splearn.domain.Email
import tobyspring.splearn.domain.Member
import tobyspring.splearn.domain.MemberRegisterRequest
import tobyspring.splearn.domain.MemberStatus
import tobyspring.splearn.domain.PasswordEncoder

class MemberServiceTest : FunSpec({

    val memberRepository = mockk<MemberRepository>()
    val emailSender = mockk<EmailSender>(relaxed = true)
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(memberRepository, emailSender, passwordEncoder)

    val request = MemberRegisterRequest(
        email = Email("test@example.com"),
        password = "password123",
        name = "홍길동"
    )

    beforeTest {
        clearAllMocks()
    }

    test("회원 가입 성공 시 저장 및 이메일 전송") {
        val encodedPassword = "encodedPassword123"

        // given
        every { memberRepository.findByEmail(request.email) } returns null
        every { passwordEncoder.encode(request.password) } returns encodedPassword
        val member = Member.register(request, passwordEncoder)
        every { memberRepository.save(any()) } returns member

        // when
        val result = memberService.register(request)

        // then
        result.email shouldBe request.email
        verify(exactly = 1) { memberRepository.save(any()) }
        verify(exactly = 1) { emailSender.send(request.email, any(), any()) }
    }

    test("중복 이메일일 경우 예외 발생") {
        // given
        every { memberRepository.findByEmail(request.email) } returns mockk<Member>()

        // when & then
        shouldThrow<DuplicateEmailException> {
            memberService.register(request)
        }
    }
})
