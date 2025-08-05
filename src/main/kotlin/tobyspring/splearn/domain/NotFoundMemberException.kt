package tobyspring.splearn.domain

class NotFoundMemberException(message: String = "회원을 찾을 수 없습니다.") : RuntimeException(message)
