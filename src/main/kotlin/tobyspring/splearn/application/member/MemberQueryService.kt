package tobyspring.splearn.application.member

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tobyspring.splearn.application.member.provided.MemberFinder
import tobyspring.splearn.application.member.required.MemberRepository
import tobyspring.splearn.domain.member.Member
import tobyspring.splearn.domain.member.NotFoundMemberException

@Service
class MemberQueryService(private val memberRepository: MemberRepository) : MemberFinder {
    @Transactional(readOnly = true)
    override fun find(memberId: Long): Member {
        return memberRepository.findById(memberId) ?: throw NotFoundMemberException()
    }
}
