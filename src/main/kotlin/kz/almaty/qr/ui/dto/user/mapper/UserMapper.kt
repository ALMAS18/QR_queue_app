package kz.almaty.qr.ui.dto.user.mapper

import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.domain.user.repository.UserRepository
import kz.almaty.qr.ui.dto.user.response.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserMapper {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun parseToResponse(user: User): UserResponse = UserResponse(
        id = user.id!!,
        email = user.email!!
    )

    fun parseToResponse(userId: Long): UserResponse = parseToResponse(userRepository.findById(userId).get())

}