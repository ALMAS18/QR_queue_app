package kz.almaty.qr.infrastructure

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class TokenUtils {

    companion object {
        private const val SECRET_KEY = "queue_qr"

        fun generateToken(email: String, password: String): String = JWT.create().withHeader(
            mapOf(
                "email" to email,
                "password" to password,
                "time" to System.currentTimeMillis()
            )
        ).sign(
            Algorithm.HMAC256("queue_qr")
        )
    }

}