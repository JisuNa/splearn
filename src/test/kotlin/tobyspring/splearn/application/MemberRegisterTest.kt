package tobyspring.splearn.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import tobyspring.splearn.application.required.EmailSender
import tobyspring.splearn.application.required.MemberRepository
import tobyspring.splearn.domain.DuplicateEmailException
import tobyspring.splearn.domain.Email
import tobyspring.splearn.domain.Member
import tobyspring.splearn.domain.MemberRegisterRequest
import tobyspring.splearn.domain.PasswordEncoder

class MemberServiceTest : FunSpec({

    val memberRepository = mockk<MemberRepository>()
    val emailSender = mockk<EmailSender>(relaxed = true)
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberModifyService = MemberModifyService(memberRepository, emailSender, passwordEncoder)

    val invalidEmail = Email("invalidEmail")
    val validEmail = Email("test@example.com")
    val mockPassword = "mockPassword"
    val mockNickname = "mockNickname"

    val memberRequest = MemberRegisterRequest(
        email = validEmail,
        password = mockPassword,
        nickname = mockNickname
    )

    beforeTest {
        clearAllMocks()
    }

    test("회원 가입 성공 시 저장 및 이메일 전송") {
        val encodedPassword = "encodedPassword123"

        // given
        every { memberRepository.findByEmail(memberRequest.email) } returns null
        every { passwordEncoder.encode(memberRequest.password) } returns encodedPassword
        val member = Member.register(memberRequest, passwordEncoder)
        every { memberRepository.save(any()) } returns member

        // when
        val result = memberModifyService.register(memberRequest)

        // then
        result.email shouldBe memberRequest.email
        verify(exactly = 1) { memberRepository.save(any()) }
        verify(exactly = 1) { emailSender.send(memberRequest.email, any(), any()) }
    }

    test("중복 이메일일 경우 예외 발생") {
        // given
        every { memberRepository.findByEmail(memberRequest.email) } returns mockk<Member>()

        // when & then
        shouldThrow<DuplicateEmailException> {
            memberModifyService.register(memberRequest)
        }
    }
})
