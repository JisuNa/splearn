package tobyspring.splearn.application.provided

import tobyspring.splearn.domain.Member
import tobyspring.splearn.domain.MemberRegisterRequest

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
interface MemberRegister {
    fun register(req: MemberRegisterRequest): Member
    fun activate(memberId: Long)
}
