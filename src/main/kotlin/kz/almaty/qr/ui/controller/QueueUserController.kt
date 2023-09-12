package kz.almaty.qr.ui.controller

import kz.almaty.qr.domain.queue.service.QueueService
import kz.almaty.qr.domain.queue_item.service.QueueItemService
import kz.almaty.qr.infrastructure.SecurityAuthorization
import kz.almaty.qr.ui.dto.queue.request.AddQueueRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws

@RestController
@CrossOrigin("http://172.20.10.3:5173", maxAge = 0)
@RequestMapping("/queue/user")
class QueueUserController {

    @Autowired
    private lateinit var queueItemService: QueueItemService

    @Autowired
    private lateinit var securityAuthorization: SecurityAuthorization

    @Autowired
    private lateinit var queueService: QueueService


    @Throws(Exception::class)
    @GetMapping("/all")
    fun getMyQueueList(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String
    ): ResponseEntity<Any> = securityAuthorization.checkToken(token) {
        queueItemService.getMyQueue(it)
    }


    @Throws(Exception::class)
    @GetMapping("/information")
    fun getQueueInformation(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestParam("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkToken(token) {
        queueService.getQueueInformationUser(it, id)
    }


    @Throws(Exception::class)
    @PostMapping("/add/{id}")
    fun addMeToQueue(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestBody addQueueRequest: AddQueueRequest,
        @PathVariable("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkToken(token) {
        queueItemService.addQueueItem(it, addQueueRequest, id)
    }


    @Throws(Exception::class)
    @DeleteMapping
    fun removeSelfFromQueue(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestParam("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkToken(token) {
        queueItemService.removeSelfFromQueue(it, id)
    }




}