package kz.almaty.qr.domain.user.model

import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String? = null,
    val password: String? = null,
    val token: String? = null
)