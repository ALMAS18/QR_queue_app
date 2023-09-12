package kz.almaty.qr.ui.dto.queue.mapper

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.queue.model.Queue
import kz.almaty.qr.domain.queue_item.repository.QueueItemRepository
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.ui.dto.manager.mapper.ManagerMapper
import kz.almaty.qr.ui.dto.queue.response.QueueInformationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class QueueInformationMapper {

    @Autowired
    private lateinit var managerMapper: ManagerMapper

    @Autowired
    private lateinit var queueItemRepository: QueueItemRepository

    @Autowired
    private lateinit var queueItemMapper: QueueItemMapper

    fun parseToResponse(
        queue: Queue,
        manager: Manager? = null,
        user: User? = null
    ): QueueInformationResponse = QueueInformationResponse(
        id = queue.id!!,
        isActive = queue.isActive,
        manager = if (manager == null) {
            managerMapper.parseToResponse(queue.managerId!!)
        } else {
            managerMapper.parseToResponse(manager)
        },
        queueItems = queueItemMapper.parseToListResponse(
            queueItemRepository.findAllByQueueId(queue.id),
            user = user
        ),
        title = queue.title!!,
        startedOn = queue.createdTime,
        finishedOn = queue.finishedTime,
        isPause = queue.isPaused
    )

    fun parseToListResponse(
        queueList: List<Queue>,
        manager: Manager? = null,
        user: User? = null
    ): List<QueueInformationResponse> = arrayListOf<QueueInformationResponse>().apply {
        for (queue in queueList) {
            add(
                parseToResponse(
                    queue = queue,
                    manager = manager,
                    user = user
                )
            )
        }
    }

}