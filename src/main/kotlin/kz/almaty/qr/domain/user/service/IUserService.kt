package kz.almaty.qr.domain.user.service

import kz.almaty.qr.ui.dto.user.request.RegisterUserRequest
import org.springframework.http.ResponseEntity

interface IUserService {

    fun register(registerUserRequest: RegisterUserRequest): ResponseEntity<Any>

}