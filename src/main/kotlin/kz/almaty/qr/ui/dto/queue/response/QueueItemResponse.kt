package kz.almaty.qr.ui.dto.queue.response

import kz.almaty.qr.ui.dto.manager.response.ManagerResponse
import kz.almaty.qr.ui.dto.user.response.UserResponse

class QueueItemResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val createdTime: Long,
    val status: String,
    val finishedTime: Long?,
    val user: UserResponse,
    val queueId: Long,
    val description: String,
    var time: Int? = null,
    var manager: ManagerResponse? = null,
    var title: String? = null
)