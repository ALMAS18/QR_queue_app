package kz.almaty.qr.domain.queue_item.service

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.domain.queue.model.Queue
import kz.almaty.qr.domain.queue.repository.QueueRepository
import kz.almaty.qr.domain.queue_item.model.QueueItem
import kz.almaty.qr.domain.queue_item.model.QueueItemStatus
import kz.almaty.qr.domain.queue_item.repository.QueueItemRepository
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.ui.dto.manager.mapper.ManagerMapper
import kz.almaty.qr.ui.dto.queue.mapper.QueueItemMapper
import kz.almaty.qr.ui.dto.queue.request.AddQueueRequest
import kz.almaty.qr.ui.dto.queue.response.QueueItemResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QueueItemService : IQueueItemService {

    @Autowired
    private lateinit var queueItemRepository: QueueItemRepository

    @Autowired
    private lateinit var queueRepository: QueueRepository

    @Autowired
    private lateinit var queueItemMapper: QueueItemMapper

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    @Autowired
    private lateinit var managerMapper: ManagerMapper

    override fun addQueueItem(user: User, addQueueRequest: AddQueueRequest, queueId: Long): ResponseEntity<Any> {
        val queueOptional = queueRepository.findById(queueId)
        if (!queueOptional.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Queue not found")
        }
        val queue = queueOptional.get()
        if (!queue.isActive) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Queue is not active")
        }
        queueItemRepository.save(
            QueueItem(
                queueId = queue.id,
                userId = user.id,
                addedTime = System.currentTimeMillis(),
                finishedTime = 0,
                firstName = addQueueRequest.firstName,
                lastName = addQueueRequest.lastName,
                phone = addQueueRequest.phoneNumber,
                email = addQueueRequest.email,
                status = QueueItemStatus.WAITING.name,
                description = addQueueRequest.description
            )
        )
        checkQueueItemStatus(queueOptional.get())
        return ResponseEntity.ok("Created")
    }

    override fun removeFromQueueItem(manager: Manager, queueItemId: Long): ResponseEntity<Any> = getQueueItemForManager(
        manager = manager,
        queueItemId = queueItemId,
    ) { queueItem, queue ->
        queueItem.status = QueueItemStatus.DEACTIVATED.name
        queueItemRepository.save(queueItem)
        checkQueueItemStatus(queue)
        ResponseEntity.ok("Deactivated")
    }

    override fun startQueueItem(manager: Manager, queueItemId: Long): ResponseEntity<Any> = getQueueItemForManager(
        manager = manager,
        queueItemId = queueItemId,
    ) { queueItem, queue ->
        queueItem.status = QueueItemStatus.IN_PROGRESS.name
        queueItemRepository.save(queueItem)
        checkQueueItemStatus(queue)
        ResponseEntity.ok("Started")
    }

    override fun finishQueueItem(manager: Manager, queueItemId: Long): ResponseEntity<Any> = getQueueItemForManager(
        manager = manager,
        queueItemId = queueItemId,
    ) { queueItem, queue ->
        queueItem.status = QueueItemStatus.FINISHED.name
        queueItem.finishedTime = System.currentTimeMillis()
        queueItemRepository.save(queueItem)
        checkQueueItemStatus(queue)
        ResponseEntity.ok("finished")
    }

    override fun removeSelfFromQueue(user: User, queueItemId: Long): ResponseEntity<Any> = getQueueItem(
        queueItemId
    ) { queueItem ->
        if (queueItem.userId == user.id) {
            queueItem.status = QueueItemStatus.DEACTIVATED.name
            queueItemRepository.save(queueItem)
            checkQueueItemStatus(queueRepository.findById(queueItem.queueId!!).get())
            ResponseEntity.ok("Deactivated")
        } else {
            ResponseEntity.ok("It's not your queue item")
        }
    }

    override fun getMyQueue(user: User): ResponseEntity<Any> {
        val response = arrayListOf<QueueItemResponse>()
        val itemList = queueItemRepository.findAllByUserId(user.id!!)
        for (item in itemList) {
            val queueItemResponse = queueItemMapper.parseToResponse(item, user)
            val queue = queueRepository.findById(item.queueId!!).get()
            checkQueueItemStatus(queue)
            queueItemResponse.manager = managerMapper.parseToResponse(
                managerRepository.findById(queue.managerId!!).get()
            )
            queueItemResponse.title = queue.title
            queueItemResponse.time = calcTime(queue.id!!, item.id!!)
            response.add(queueItemResponse)
        }
        return ResponseEntity.ok(response)
    }

    private fun calcTime(queueId: Long, itemId: Long): Int {
        val queueList = queueItemRepository.findAllByQueueId(queueId)
        var time = 5
        for (queue in queueList) {
            if (queue.id == itemId) {
                break
            }
            if (queue.status == QueueItemStatus.WAITING.name) {
                time += 5
            }
        }
        return time
    }

    private fun getQueueItemForManager(
        manager: Manager,
        queueItemId: Long,
        operation: ((QueueItem, Queue) -> ResponseEntity<Any>)
    ): ResponseEntity<Any> = getQueueItem(queueItemId) { queueItem ->
        val queueOptional = queueRepository.findById(queueItem.queueId!!)
        if (!queueOptional.isPresent) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("Queue not found")
        }
        if (queueOptional.get().managerId != manager.id) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("It's not your queue")
        }
        operation.invoke(queueItem, queueOptional.get())
    }

    private fun getQueueItem(
        queueItemId: Long,
        foundOperation: ((QueueItem) -> ResponseEntity<Any>)
    ): ResponseEntity<Any> {
        val queueItemOptional = queueItemRepository.findById(queueItemId)
        if (!queueItemOptional.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Queue item not found")
        }
        return foundOperation.invoke(queueItemOptional.get())
    }

    private fun checkQueueItemStatus(queue: Queue) {
        val queueItems = queueItemRepository.findAllByQueueId(queue.id!!)
        var isThis = false
        if (queueItems.size == 1) {
            val item = queueItems.first()
            if (item.status == QueueItemStatus.WAITING.name) {
                item.status = QueueItemStatus.ON_START.name
                queueItemRepository.save(item)
                return
            }
        }
        for (queueItem in queueItems) {
            if (queueItem.status != QueueItemStatus.WAITING.name) {
                if (queueItem.status == QueueItemStatus.ON_START.name) {
                    return
                }
                isThis = true
            }
            if (queueItem.status == QueueItemStatus.WAITING.name && isThis) {
                queueItem.status = QueueItemStatus.ON_START.name
                queueItemRepository.save(queueItem)
                return
            }
        }
    }
}