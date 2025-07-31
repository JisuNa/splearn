package tobyspring.splearn.application

import org.springframework.stereotype.Service
import tobyspring.splearn.application.provided.MemberRegister
import tobyspring.splearn.application.required.EmailSender
import tobyspring.splearn.application.required.MemberRepository
import tobyspring.splearn.domain.Member
import tobyspring.splearn.domain.MemberRegisterRequest
import tobyspring.splearn.domain.PasswordEncoder

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val emailSender: EmailSender,
    private val passwordEncoder: PasswordEncoder
) : MemberRegister {
    override fun register(req: MemberRegisterRequest): Member {
        return memberRepository.save(Member.register(req = req, passwordEncoder = passwordEncoder))
            .also {
                emailSender.send(
                    email = it.email,
                    subject = "등록을 완료해주세요.",
                    body = "아래 링크를 클릭해서 등록을 완료해주세요"
                )
            }
    }
}
