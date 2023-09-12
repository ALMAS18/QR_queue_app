package kz.almaty.qr.domain.manager.service

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.domain.user.repository.UserRepository
import kz.almaty.qr.infrastructure.TokenUtils
import kz.almaty.qr.ui.dto.manager.request.RegisterManagerRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ManagerService : IManagerService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    override fun registerManagerRequest(registerManagerRequest: RegisterManagerRequest): ResponseEntity<Any> {
        val managerOptional = managerRepository.findByEmail(registerManagerRequest.email)
        if (!managerOptional.isPresent) {
            val userOptional = userRepository.findByEmail(registerManagerRequest.email)
            if (userOptional.isPresent) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered")
            } else {
                managerRepository.save(
                    Manager(
                        email = registerManagerRequest.email,
                        password = registerManagerRequest.password,
                        firstName = registerManagerRequest.firstName,
                        lastName = registerManagerRequest.lastName,
                        token = TokenUtils.generateToken(
                            email = registerManagerRequest.email,
                            password = registerManagerRequest.password
                        )
                    )
                )
                return ResponseEntity.ok("Created")
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered")
    }

}