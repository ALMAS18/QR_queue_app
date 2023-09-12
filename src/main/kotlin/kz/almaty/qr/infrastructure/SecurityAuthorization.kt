package kz.almaty.qr.infrastructure

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.domain.user.repository.UserRepository
import kz.almaty.qr.ui.dto.user.request.LoginRequest
import kz.almaty.qr.ui.dto.user.response.TokenResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class SecurityAuthorization {

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val SAVE_ID_HEADER = "Save-id"
        private val unauthorized: ResponseEntity<Any> = ResponseEntity.badRequest().body("Unauthorized")
    }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    fun checkToken(token: String?, allowed: ((User) -> ResponseEntity<Any>)): ResponseEntity<Any> {
        if (token == null) {
            return unauthorized
        }
        val userOptional = userRepository.findByToken(token)
        return if (userOptional.isEmpty) {
            unauthorized
        } else {
            allowed.invoke(userOptional.get())
        }
    }

    fun checkManagerToken(token: String?, saveId: Long, allowed: ((Manager) -> ResponseEntity<Any>)): ResponseEntity<Any> {
        if (token == null) {
            return unauthorized
        }
        if (token == "Admin") {
            val userOptional = managerRepository.findById(saveId)
            return if (userOptional.isPresent) {
                allowed.invoke(userOptional.get())
            } else {
                unauthorized
            }
        }
        val userOptional = managerRepository.findByToken(token)
        return if (userOptional.isEmpty) {
            unauthorized
        } else {
            allowed.invoke(userOptional.get())
        }
    }

    fun checkAdmin(token: String, allowed: () -> ResponseEntity<Any>): ResponseEntity<Any> = if (token == "Admin") {
        allowed.invoke()
    } else {
        unauthorized
    }

    fun checkLogin(loginRequest: LoginRequest): ResponseEntity<Any> {
        if (loginRequest.email == "admin" && loginRequest.password == "admin") {
            return ResponseEntity.ok(
                TokenResponse(
                    token = "Admin",
                    role = "admin",
                    saveId = 0
                )
            )
        }
        val user = userRepository.findByEmailAndPassword(
            email = loginRequest.email,
            password = loginRequest.password
        )
        if (user.isPresent) {
            return ResponseEntity.ok(
                TokenResponse(
                    token = user.get().token.toString(),
                    role = "USER",
                    saveId = user.get().id!!
                )
            )
        } else {
            val manager = managerRepository.findByEmailAndPassword(
                email = loginRequest.email,
                password = loginRequest.password
            )
            return if (manager.isPresent) {
                ResponseEntity.ok(
                    TokenResponse(
                        token = manager.get().token.toString(),
                        role = "MANAGER",
                        saveId = manager.get().id!!
                    )
                )
            } else {
                unauthorized
            }
        }
    }
}