package kz.almaty.qr.domain.queue.model

import javax.persistence.*

@Entity
@Table(name = "queue_list")
class Queue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val managerId: Long? = null,
    val createdTime: Long = 0,
    var finishedTime: Long = 0,
    var isActive: Boolean = true,
    var title: String? = null,
    var isPaused: Boolean = false
)