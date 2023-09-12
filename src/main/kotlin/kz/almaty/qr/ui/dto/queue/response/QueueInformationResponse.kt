package kz.almaty.qr.ui.dto.queue.response

import kz.almaty.qr.ui.dto.manager.response.ManagerResponse

class QueueInformationResponse(
    val id: Long,
    val isActive: Boolean,
    val queueItems: List<QueueItemResponse>,
    val manager: ManagerResponse,
    val title: String,
    val startedOn: Long,
    val finishedOn: Long,
    val isPause: Boolean
)