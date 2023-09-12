package kz.almaty.qr.ui.dto.manager.mapper

import kz.almaty.qr.domain.manager.model.Manager
import kz.almaty.qr.domain.manager.repository.ManagerRepository
import kz.almaty.qr.ui.dto.manager.response.ManagerResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ManagerMapper {

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    fun parseToResponse(manager: Manager): ManagerResponse = ManagerResponse(
        id = manager.id!!,
        email = manager.email!!,
        firstName = manager.firstName!!,
        lastName = manager.lastName!!
    )

    fun parseToResponse(managerId: Long): ManagerResponse = parseToResponse(managerRepository.findById(managerId).get())

}