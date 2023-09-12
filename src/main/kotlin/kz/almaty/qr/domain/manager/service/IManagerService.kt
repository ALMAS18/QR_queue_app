package kz.almaty.qr.domain.manager.service

import kz.almaty.qr.ui.dto.manager.request.RegisterManagerRequest
import org.springframework.http.ResponseEntity

interface IManagerService {

    fun registerManagerRequest(registerManagerRequest: RegisterManagerRequest): ResponseEntity<Any>

}