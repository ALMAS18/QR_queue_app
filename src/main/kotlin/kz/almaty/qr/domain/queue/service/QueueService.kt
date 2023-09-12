package kz.almaty.qr.domain.queue.service

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.domain.queue.model.Queue
import kz.almaty.qr.domain.queue.repository.QueueRepository
import kz.almaty.qr.domain.queue_item.model.QueueItemStatus
import kz.almaty.qr.domain.queue_item.repository.QueueItemRepository
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.ui.dto.manager.mapper.ManagerMapper
import kz.almaty.qr.ui.dto.queue.mapper.QueueInformationMapper
import kz.almaty.qr.ui.dto.queue.mapper.QueueItemMapper
import kz.almaty.qr.ui.dto.queue.request.CreateQueueRequest
import kz.almaty.qr.ui.dto.queue.request.PauseRequest
import kz.almaty.qr.ui.dto.queue.response.AnotherUserResponse
import kz.almaty.qr.ui.dto.queue.response.InformationForQueueItemResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QueueService : IQueueService {

    @Autowired
    private lateinit var queueRepository: QueueRepository

    @Autowired
    private lateinit var queueItemRepository: QueueItemRepository

    @Autowired
    private lateinit var queueInformationMapper: QueueInformationMapper

    @Autowired
    private lateinit var queueItemMapper: QueueItemMapper

    @Autowired
    private lateinit var managerMapper: ManagerMapper

    override fun getQueueInformationUser(user: User, queueItemId: Long): ResponseEntity<Any> {
        val queueItemOptional = queueItemRepository.findById(queueItemId)
        if (!queueItemOptional.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Queue not found")
        }
        val queueItem = queueItemOptional.get()
        if (queueItem.userId != user.id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It's not your queue")
        }
        val queueItemResponse = queueItemMapper.parseToResponse(queueItem, user)
        val queue = queueRepository.findById(queueItemResponse.queueId).get()
        queueItemResponse.title = queue.title
        queueItemResponse.manager = managerMapper.parseToResponse(queue.managerId!!)
        val queueItems = queueItemRepository.findAllByQueueId(queue.id!!)
        val beforeUsers = arrayListOf<AnotherUserResponse>()
        for (item in queueItems) {
            if (item.id == queueItem.id) {
                break
            }
            if (item.status == QueueItemStatus.WAITING.name || item.status == QueueItemStatus.ON_START.name) {
                beforeUsers.add(
                    AnotherUserResponse(
                        firstName = item.firstName!!,
                        lastName = item.lastName!!
                    )
                )
            }
        }
        queueItemResponse.time = beforeUsers.size * 5 + 5
        return ResponseEntity.ok(
            InformationForQueueItemResponse(
                queueItemResponse = queueItemResponse,
                queueFront = beforeUsers
            )
        )
    }

    override fun getQueueInformationManager(manager: Manager, queueId: Long): ResponseEntity<Any> = getQueueForManager(
        manager = manager,
        queueId = queueId
    ) { queue ->
        ResponseEntity.ok(queueInformationMapper.parseToResponse(queue, manager))
    }

    override fun createQueue(manager: Manager, createQueueRequest: CreateQueueRequest): ResponseEntity<Any> = ResponseEntity.ok(
        queueInformationMapper.parseToResponse(
            queue = queueRepository.save(
                Queue(
                    title = createQueueRequest.title,
                    managerId = manager.id,
                    createdTime = System.currentTimeMillis(),
                    isActive = true
                )
            ),
            manager = manager
        )
    )

    override fun deactivateQueue(manager: Manager, queueId: Long): ResponseEntity<Any> = getQueueForManager(
    manager = manager,
    queueId = queueId
    ) { queue ->
        queue.isActive = false
        queue.finishedTime = System.currentTimeMillis()
        queueRepository.save(queue)
        val queueItems = queueItemRepository.findAllByQueueId(queueId)
        for (queueItem in queueItems) {
            queueItem.status = QueueItemStatus.DEACTIVATED.name
            queueItemRepository.save(queueItem)
        }
        ResponseEntity.ok("Deactivated")
    }

    override fun getMyQueue(manager: Manager): ResponseEntity<Any> = ResponseEntity.ok(
        queueInformationMapper.parseToListResponse(
            queueList = queueRepository.findAllByManagerId(manager.id!!).sortedBy { !it.isActive },
            manager = manager
        )
    )

    override fun setPause(
        manager: Manager,
        pauseRequest: PauseRequest
    ): ResponseEntity<Any> = getQueueForManager(manager, pauseRequest.queueId) {
        it.isPaused = pauseRequest.isPause
        queueRepository.save(it)
        ResponseEntity.ok("Saved")
    }

    private fun getQueueForManager(
        manager: Manager,
        queueId: Long,
        operation: ((Queue) -> ResponseEntity<Any>)
    ): ResponseEntity<Any> {
        val queueOptional = queueRepository.findById(queueId)
        if (!queueOptional.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Queue not found")
        }
        val queue = queueOptional.get()
        if (queue.managerId != manager.id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It's not your queue")
        }
        return operation.invoke(queue)
    }
}
