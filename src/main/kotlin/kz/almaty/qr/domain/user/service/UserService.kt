package kz.almaty.qr.domain.user.service

import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.domain.user.repository.UserRepository
import kz.almaty.qr.infrastructure.TokenUtils
import kz.almaty.qr.ui.dto.user.request.RegisterUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService : IUserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    override fun register(registerUserRequest: RegisterUserRequest): ResponseEntity<Any> {
        val userOptional = userRepository.findByEmail(registerUserRequest.email)
        if (userOptional.isPresent) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered")
        }
        val managerOptional = managerRepository.findByEmail(registerUserRequest.email)
        if (managerOptional.isPresent) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered")
        }
        userRepository.save(
            User(
                email = registerUserRequest.email,
                password = registerUserRequest.password,
                token = TokenUtils.generateToken(
                    email = registerUserRequest.email,
                    password = registerUserRequest.password
                )
            )
        )
        return ResponseEntity.ok("Created")
    }
}