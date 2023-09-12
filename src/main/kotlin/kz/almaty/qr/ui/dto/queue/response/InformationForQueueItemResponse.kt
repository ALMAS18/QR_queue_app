package kz.almaty.qr.ui.dto.queue.response

class InformationForQueueItemResponse(
    val queueItemResponse: QueueItemResponse,
    val queueFront: List<AnotherUserResponse>,
)