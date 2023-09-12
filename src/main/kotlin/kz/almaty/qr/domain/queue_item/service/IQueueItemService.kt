package kz.almaty.qr.domain.queue_item.service

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.ui.dto.queue.request.AddQueueRequest
import org.springframework.http.ResponseEntity

interface IQueueItemService {

    fun addQueueItem(user: User, addQueueRequest: AddQueueRequest, queueId: Long): ResponseEntity<Any>

    fun removeFromQueueItem(manager: Manager, queueItemId: Long): ResponseEntity<Any>

    fun startQueueItem(manager: Manager, queueItemId: Long): ResponseEntity<Any>

    fun finishQueueItem(manager: Manager, queueItemId: Long): ResponseEntity<Any>

    fun removeSelfFromQueue(user: User, queueItemId: Long): ResponseEntity<Any>

    fun getMyQueue(user: User): ResponseEntity<Any>

}