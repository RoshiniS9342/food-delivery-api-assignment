package com.resaturant_assignment.Restaurant_Api_Group_Assignment.application

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.*
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service.CustomerService
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service.RestaurantService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/customer")
class CustomerController(private val customerService: CustomerService,
    private val restaurantService: RestaurantService) {


    @PostMapping("/register")
    fun register(@RequestBody customer: Customer): ResponseEntity<*> {
        val response = customerService.addCustomer(customer)
        return ResponseEntity<Customer>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val response = customerService.login(loginRequest)
        return ResponseEntity<LoginResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/list/allRestaurants")
    fun getAllRest() : ResponseEntity<*>? {
        val rests: List<Restaurant?> = restaurantService.getAllRest()
        return ResponseEntity<List<Restaurant?>>(rests, HttpStatus.OK)
    }

    @GetMapping("/restaurants/menu")
    fun getRestMenuById(@RequestParam(value = "id") restId: String) : ResponseEntity<*>? {
        val restMenu = restaurantService.getRestMenuById(restId)
        return ResponseEntity<Restaurant?>(restMenu, HttpStatus.OK)
    }

    @PostMapping("/create/orders")
    fun addOrder(@RequestBody orderDetails: Orders,
                 @RequestParam(value = "restaurantId") restId: String?): ResponseEntity<*>? {
        val response = restaurantService.updateOrder(orderDetails, restId)
        return ResponseEntity<String>(response, HttpStatus.CREATED)
    }

    @GetMapping("/orderDetails")
    fun getOrderById(@RequestParam(value = "orderId") orderId: String): ResponseEntity<*>? {
        val response = restaurantService.getOrderById(orderId)
        return ResponseEntity<Orders>(response, HttpStatus.OK)
    }

    @GetMapping("/track/order/status")
    fun getOrderStatus(@RequestParam(value = "orderId") orderId: String): ResponseEntity<*>? {
        val response = restaurantService.getOrderStatus(orderId)
        return ResponseEntity<UpdateStatus>(response, HttpStatus.OK)
    }

    @DeleteMapping("/order")
    fun deleteOrderById(@RequestParam(value = "orderId") orderId: String): ResponseEntity<*>? {
        val response = restaurantService.deleteOrderById(orderId)
        return ResponseEntity<String>(response, HttpStatus.OK)
    }

}