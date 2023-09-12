package kz.almaty.qr.domain.manager.repository

import kz.almaty.qr.domain.manager.model.Manager
import org.apache.catalina.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface ManagerRepository : CrudRepository<Manager, Long> {

    fun findByToken(token: String): Optional<Manager>

    fun findByEmailAndPassword(email: String, password: String): Optional<Manager>

    fun findByEmail(email: String): Optional<Manager>

}