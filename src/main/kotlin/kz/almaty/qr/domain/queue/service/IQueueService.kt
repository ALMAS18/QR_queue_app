package kz.almaty.qr.domain.queue.service

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.ui.dto.queue.request.CreateQueueRequest
import kz.almaty.qr.ui.dto.queue.request.PauseRequest
import org.springframework.http.ResponseEntity

interface IQueueService {

    fun getQueueInformationUser(user: User, queueItemId: Long): ResponseEntity<Any>

    fun getQueueInformationManager(manager: Manager, queueId: Long): ResponseEntity<Any>

    fun createQueue(manager: Manager, createQueueRequest: CreateQueueRequest): ResponseEntity<Any>

    fun deactivateQueue(manager: Manager, queueId: Long): ResponseEntity<Any>

    fun getMyQueue(manager: Manager): ResponseEntity<Any>

    fun setPause(manager: Manager, pauseRequest: PauseRequest): ResponseEntity<Any>

}