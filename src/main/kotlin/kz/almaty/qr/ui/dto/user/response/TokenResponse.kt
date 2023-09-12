package kz.almaty.qr.ui.dto.user.response

class TokenResponse(
    val token: String,
    val role: String,
    val saveId: Long
)