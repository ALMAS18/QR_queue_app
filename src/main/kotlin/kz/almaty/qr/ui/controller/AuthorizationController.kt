package kz.almaty.qr.ui.controller

import kz.almaty.qr.domain.manager.service.ManagerService
import kz.almaty.qr.domain.user.service.UserService
import kz.almaty.qr.infrastructure.SecurityAuthorization
import kz.almaty.qr.ui.dto.manager.request.RegisterManagerRequest
import kz.almaty.qr.ui.dto.user.request.LoginRequest
import kz.almaty.qr.ui.dto.user.request.RegisterUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws

@RestController
@CrossOrigin("http://172.20.10.3:5173", maxAge = 0)
@RequestMapping("/authorization")
class AuthorizationController  {

    @Autowired
    private lateinit var securityAuthorization: SecurityAuthorization

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var managerService: ManagerService

    @Throws(Exception::class)
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> = securityAuthorization.checkLogin(loginRequest)


    @Throws(Exception::class)
    @PostMapping("/registration/manager")
    fun registerManager(
        @RequestBody registerManagerRequest: RegisterManagerRequest
    ): ResponseEntity<Any> = managerService.registerManagerRequest(registerManagerRequest)


    @Throws(Exception::class)
    @PostMapping("/registration/user")
    fun registerManager(
        @RequestBody registerUserRequest: RegisterUserRequest
    ): ResponseEntity<Any> = userService.register(registerUserRequest)

}