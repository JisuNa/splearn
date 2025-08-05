package tobyspring.splearn.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tobyspring.splearn.application.provided.MemberFinder
import tobyspring.splearn.application.required.MemberRepository
import tobyspring.splearn.domain.Member
import tobyspring.splearn.domain.NotFoundMemberException

@Service
class MemberQueryService(private val memberRepository: MemberRepository) : MemberFinder {
    @Transactional(readOnly = true)
    override fun find(memberId: Long): Member {
        return memberRepository.findById(memberId) ?: throw NotFoundMemberException()
    }
}
