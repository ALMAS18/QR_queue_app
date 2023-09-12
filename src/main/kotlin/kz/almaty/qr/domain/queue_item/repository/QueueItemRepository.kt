package kz.almaty.qr.domain.queue_item.repository

import kz.almaty.qr.domain.queue_item.model.QueueItem
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QueueItemRepository : CrudRepository<QueueItem, Long> {

    fun findAllByQueueId(queueId: Long): MutableList<QueueItem>

    fun findAllByUserId(userId: Long): MutableList<QueueItem>

}