package kz.almaty.qr.ui.controller

import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.domain.queue.repository.QueueRepository
import kz.almaty.qr.domain.queue_item.repository.QueueItemRepository
import kz.almaty.qr.infrastructure.SecurityAuthorization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws

@RestController
@CrossOrigin("http://172.20.10.3:5173", maxAge = 0)
@RequestMapping("/admin")
class AdminController {

    @Autowired
    private lateinit var securityAuthorization: SecurityAuthorization

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    @Autowired
    private lateinit var queueRepository: QueueRepository

    @Autowired
    private lateinit var queueItemRepository: QueueItemRepository

    @Throws(Exception::class)
    @GetMapping("/managers")
    fun findAllManagers(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String
    ) : ResponseEntity<Any> = securityAuthorization.checkAdmin(token) {
        ResponseEntity.ok(managerRepository.findAll())
    }

    @Throws(Exception::class)
    @DeleteMapping("/manager")
    fun deleteManager(
        @RequestHeader(SecurityAuthorization.TOKEN_HEADER) token: String,
        @RequestParam("id") id: Long
    ) : ResponseEntity<Any> = securityAuthorization.checkAdmin(token) {
        val managerOptional = managerRepository.findById(id)
        if (!managerOptional.isPresent) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found")
        }
        val manager = managerOptional.get()
        val queueList = queueRepository.findAllByManagerId(id)
        for (queue in queueList) {
            val queueItems = queueItemRepository.findAllByQueueId(queue.id!!)
            for (queueItem in queueItems) {
                queueItemRepository.delete(queueItem)
            }
            queueRepository.delete(queue)
        }
        managerRepository.delete(manager)
        ResponseEntity.ok("Deleted")
    }

}