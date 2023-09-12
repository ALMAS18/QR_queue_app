package kz.almaty.qr.ui.dto.queue.mapper

import kz.almaty.qr.domain.queue_item.model.QueueItem
import kz.almaty.qr.domain.user.model.User
import kz.almaty.qr.ui.dto.queue.response.QueueItemResponse
import kz.almaty.qr.ui.dto.user.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class QueueItemMapper {

    @Autowired
    private lateinit var userMapper: UserMapper

    fun parseToResponse(
        queueItem: QueueItem,
        user: User? = null
    ): QueueItemResponse {
        println()
        return QueueItemResponse(
            id = queueItem.id!!,
            firstName = queueItem.firstName!!,
            lastName = queueItem.lastName!!,
            email = queueItem.email!!,
            phoneNumber = queueItem.phone!!,
            createdTime = queueItem.addedTime!!,
            status = queueItem.status!!,
            finishedTime = queueItem.finishedTime!!,
            queueId = queueItem.queueId!!,
            description = queueItem.description!!,
            user = if (user == null) {
                userMapper.parseToResponse(queueItem.userId!!)
            } else {
                userMapper.parseToResponse(user)
            }
        )
    }

    fun parseToListResponse(
        queueItemList: List<QueueItem>,
        user: User? = null
    ): List<QueueItemResponse> = arrayListOf<QueueItemResponse>().apply {
        for (queueItem in queueItemList) add(parseToResponse(queueItem, user))
    }

}