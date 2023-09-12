package kz.almaty.qr.domain.queue.repository

import kz.almaty.qr.domain.queue.model.Queue
import org.springframework.data.repository.CrudRepository

interface QueueRepository : CrudRepository<Queue, Long> {

    fun findAllByManagerId(managerId: Long): MutableList<Queue>

}