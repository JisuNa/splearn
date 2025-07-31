package tobyspring.splearn.application

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
import tobyspring.splearn.domain.Email
import tobyspring.splearn.domain.MemberRegisterRequest
import tobyspring.splearn.domain.MemberStatus
import tobyspring.splearn.domain.PasswordEncoder

class MemberRegisterTest : FunSpec({
    val memberRepository = mockk<MemberRepository>()
    val emailSender = mockk<EmailSender>(relaxed = true)
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(memberRepository, emailSender, passwordEncoder)

    beforeEach {
        clearAllMocks()
    }

    test("회원 등록 시 암호화된 비밀번호로 Member를 생성한다") {
        // given
        val email = Email("test@example.com")
        val request = MemberRegisterRequest(
            email = email,
            name = "테스트유저",
            password = "rawPassword123"
        )
        val encodedPassword = "encodedPassword123"

        every { passwordEncoder.encode("rawPassword123") } returns encodedPassword
        every {
            emailSender.send(
                email = email,
                subject = "등록을 완료해주세요.",
                body = "아래 링크를 클릭해서 등록을 완료해주세요"
            )
        } just Runs

        // when
        val result = memberService.register(request)

        // then
        result.email shouldBe email
        result.nickname shouldBe "테스트유저"
        result.status shouldBe MemberStatus.PENDING

        verify(exactly = 1) { passwordEncoder.encode("rawPassword123") }
    }
})
