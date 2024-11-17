package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.DeliveryPerson
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository.DeliveryRepo
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository.RestRepo
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val restRepo: RestRepo// Your repository to fetch manager details
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val restaurant = restRepo.findAll().find { it.manager?.username == username }
            ?: throw Exception("User not found")

        return User(
            restaurant.manager?.username,
            restaurant.manager?.password,
            emptyList() // Authorities can be added here if required
        )
    }
}



