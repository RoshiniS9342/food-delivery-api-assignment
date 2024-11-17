package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.Customer
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.LoginRequest
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.LoginResponse

interface CustomerService {
    fun addCustomer(customer: Customer): Customer

    fun login(loginRequest: LoginRequest): LoginResponse
}