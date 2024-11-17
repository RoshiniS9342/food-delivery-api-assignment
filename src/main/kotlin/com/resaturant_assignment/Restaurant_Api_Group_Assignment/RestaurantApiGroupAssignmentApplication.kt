package com.resaturant_assignment.Restaurant_Api_Group_Assignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class RestaurantApiGroupAssignmentApplication

fun main(args: Array<String>) {
	runApplication<RestaurantApiGroupAssignmentApplication>(*args)
}
