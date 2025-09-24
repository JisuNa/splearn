package tobyspring.splearn.adapter.security

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BcryptPasswordEncoderTest : FunSpec({

    test("패스워드 인코딩 및 검증 성공") {
        val encoder = BcryptPasswordEncoder()
        val password = "testPassword123"

        val encodedPassword = encoder.encode(password)

        encoder.matches(password, encodedPassword) shouldBe true
    }

    test("잘못된 패스워드로 검증 실패") {
        val encoder = BcryptPasswordEncoder()
        val password = "testPassword123"
        val wrongPassword = "wrongPassword456"

        val encodedPassword = encoder.encode(password)

        encoder.matches(wrongPassword, encodedPassword) shouldBe false
    }

})
