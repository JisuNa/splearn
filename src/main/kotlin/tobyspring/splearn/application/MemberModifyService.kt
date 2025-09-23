package tobyspring.splearn.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tobyspring.splearn.application.provided.MemberRegister
import tobyspring.splearn.application.required.EmailSender
import tobyspring.splearn.application.required.MemberRepository
import tobyspring.splearn.domain.DuplicateEmailException
import tobyspring.splearn.domain.Email
import tobyspring.splearn.domain.Member
import tobyspring.splearn.domain.MemberRegisterRequest
import tobyspring.splearn.domain.PasswordEncoder

@Service
class MemberModifyService(
    private val memberQueryService: MemberQueryService,
    private val memberRepository: MemberRepository,
    private val emailSender: EmailSender,
    private val passwordEncoder: PasswordEncoder
) : MemberRegister {

    @Transactional
    override fun register(req: MemberRegisterRequest): Member {

        checkDuplicateEmail(req.email)

        return memberRepository.save(Member.register(req = req, passwordEncoder = passwordEncoder))
            .also { sendWelcomeEmail(it.email) }
    }

    private fun checkDuplicateEmail(email: Email) {
        if (memberRepository.findByEmail(email) != null) {
            throw DuplicateEmailException("이미 사용중인 이메일입니다.")
        }
    }

    private fun sendWelcomeEmail(email: Email) {
        emailSender.send(
            email = email,
            subject = "등록을 완료해주세요.",
            body = "아래 링크를 클릭해서 등록을 완료해주세요"
        )
    }

    @Transactional
    override fun activate(memberId: Long) {
        memberQueryService.find(memberId)
            .apply { activate() }
    }
}
