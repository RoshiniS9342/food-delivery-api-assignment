package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.config.JwtUtil
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.Customer
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.LoginRequest
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.LoginResponse
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository.CustomerRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CustomerServiceImpl(private val customerRepo: CustomerRepo,
                          private val passwordEncoder: PasswordEncoder
) : CustomerService{
    @Autowired
    private lateinit var jwtUtil: JwtUtil

   override fun addCustomer(customer: Customer): Customer {
       customer.password = passwordEncoder.encode(customer.password)
        return customerRepo.save(customer)
    }

    override fun login(loginRequest: LoginRequest): LoginResponse {
        val customer = customerRepo.findAll().find { it.username == loginRequest.username }

        if (!passwordEncoder.matches(loginRequest.password,customer?.password)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "password incorrect")
        }
        val token = customer?.password?.let { jwtUtil.generateToken(it) }
        return LoginResponse(token)
    }
}