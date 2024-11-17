package com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.Restaurant
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RestRepo : MongoRepository<Restaurant, String> {

}
