package kz.almaty.qr.domain.queue_item.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "queue_item")
class QueueItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val queueId: Long? = null,
    val userId: Long? = null,
    val addedTime: Long? = null,
    var finishedTime: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var status: String? = QueueItemStatus.WAITING.name,
    val description: String? = null
)