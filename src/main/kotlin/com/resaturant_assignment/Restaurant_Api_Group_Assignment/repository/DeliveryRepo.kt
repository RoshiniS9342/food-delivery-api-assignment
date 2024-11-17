package com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.DeliveryPerson
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface DeliveryRepo : MongoRepository<DeliveryPerson, String> {
    fun findByUsername(username: String?): Optional<DeliveryPerson?>?
}