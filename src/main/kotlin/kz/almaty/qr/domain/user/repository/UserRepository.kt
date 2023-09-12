package kz.almaty.qr.domain.user.repository

import kz.almaty.qr.domain.user.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun findByToken(token: String): Optional<User>

    fun findByEmailAndPassword(email: String, password: String): Optional<User>

    fun findByEmail(email: String): Optional<User>

}