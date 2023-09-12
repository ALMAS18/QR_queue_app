package kz.almaty.qr.ui.controller

import kz.almaty.qr.domain.queue.service.QueueService
import kz.almaty.qr.domain.queue_item.service.QueueItemService
import kz.almaty.qr.infrastructure.SecurityAuthorization
import kz.almaty.qr.ui.dto.queue.request.CreateQueueRequest
import kz.almaty.qr.ui.dto.queue.request.LongRequest
import kz.almaty.qr.ui.dto.queue.request.PauseRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws

@RestController
@CrossOrigin("http://172.20.10.3:5173", maxAge = 0)
@RequestMapping("/queue/manager")
class ManagerQueueController {

    @Autowired
    private lateinit var securityAuthorization: SecurityAuthorization

    @Autowired
    private lateinit var queueItemService: QueueItemService

    @Autowired
    private lateinit var queueService: QueueService


    @Throws(Exception::class)
    @GetMapping("/all")
    fun getMyQueueList(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueService.getMyQueue(it)
    }


    @Throws(Exception::class)
    @PostMapping("/create")
    fun createQueueRequest(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestBody createQueueRequest: CreateQueueRequest
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueService.createQueue(it, createQueueRequest)
    }


    @Throws(Exception::class)
    @DeleteMapping
    fun removeFromQueue(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestParam("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueItemService.removeFromQueueItem(it, id)
    }


    @Throws(Exception::class)
    @PostMapping("/start")
    fun startQueueItem(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestParam("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueItemService.startQueueItem(it, id)
    }


    @Throws(Exception::class)
    @PostMapping("/finish")
    fun finishQueueItem(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestParam("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueItemService.finishQueueItem(it, id)
    }


    @Throws(Exception::class)
    @GetMapping("/information")
    fun getQueueInformation(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestParam("id") id: Long
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueService.getQueueInformationManager(it, id)
    }


    @Throws(Exception::class)
    @PostMapping("/disable")
    fun disableQueue(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestBody longRequest: LongRequest
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueService.deactivateQueue(it, longRequest.body)
    }

    @Throws(Exception::class)
    @PostMapping("/pause")
    fun pauseController(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestHeader(SecurityAuthorization.SAVE_ID_HEADER) saveId: Long,
        @RequestBody pauseRequest: PauseRequest
    ): ResponseEntity<Any> = securityAuthorization.checkManagerToken(token, saveId) {
        queueService.setPause(it, pauseRequest)
    }
}