package com.resaturant_assignment.Restaurant_Api_Group_Assignment.application

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.*
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.UpdateStatus
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service.RestaurantService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
class controller (
    var restaurantService: RestaurantService
){

    //Restaurant
    @PostMapping("/add/restaurant")
    fun addRest(@RequestBody restaurant: Restaurant): ResponseEntity<*> {
        if(restaurant.name.isNullOrEmpty() || restaurant.address.isNullOrEmpty() || restaurant.phoneNumber.isNullOrEmpty())
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Mandatory fields required")

       val savedRestaurant = restaurantService.addRest(restaurant)
        return ResponseEntity<Restaurant>(savedRestaurant, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant")
    fun getRestById(@RequestParam(value = "id") restId: String) : ResponseEntity<*>? {
        val resposne = restaurantService.getRestById(restId)
           return ResponseEntity<Restaurant>(resposne, HttpStatus.OK)
    }



    //Menu

    @PutMapping("restaurant/add/food-items")
    fun addFoodItems(
        @RequestParam(value = "id") restId: String,
        @RequestBody foodItem: FoodItem): ResponseEntity<*>? {
       return restaurantService.addFoodItem(foodItem, restId)
            ?.map { rest -> ResponseEntity<Restaurant>(rest, HttpStatus.OK) }
            ?.orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant not found")
            }
    }

    @DeleteMapping("/restaurant/food-items")
    fun deleteRestFoodById(@RequestParam(value = "restaurantId") restId: String?,
                       @RequestParam(value = "foodId") foodId: String?) : ResponseEntity<*>? {
        val response = restaurantService.deleteFoodById(restId, foodId)
        return ResponseEntity<Restaurant>(response, HttpStatus.OK)
    }

    @PatchMapping("/order/updateStatus")
    fun updateOrderStatus(@RequestBody status: UpdateStatus,
                          @RequestParam(value = "orderId") orderId: String,
                          ): ResponseEntity<*>? {
        val response = restaurantService.updateOrderStatus(orderId, status)
        return ResponseEntity<String>(response, HttpStatus.OK)
    }

    @GetMapping("/order/status")
    fun getOrderStatus(@RequestParam(value = "orderId") orderId: String): ResponseEntity<*>? {
        val response = restaurantService.getOrderStatus(orderId)
        return ResponseEntity<UpdateStatus>(response, HttpStatus.OK)
    }


    //Restaurant Manager

    @PostMapping("/restaurant/manager/register")
    fun registerManger(@RequestBody manager: Manager,
                       @RequestParam restId: String) : ResponseEntity<*>? {
        val response = restaurantService.registerManager(restId, manager)
        return ResponseEntity<String>(response, HttpStatus.OK)
    }

    @GetMapping("/restaurant/manager/login")
    fun loginManger(@RequestBody loginRequest: LoginRequest,
                    @RequestParam(value = "id") restId: String) : ResponseEntity<*>? {
        val response = restaurantService.login(loginRequest, restId)
        return ResponseEntity<LoginResponse>(response, HttpStatus.OK)
    }

    //Delivery
    @GetMapping("/delivery/list/allOrders")
    fun getAllOrders(): ResponseEntity<*>? {
        val response = restaurantService.getAllOrders()
        return ResponseEntity<List<Orders>>(response, HttpStatus.OK)
    }

    @PutMapping("/delivery/select/order")
    fun selectOrder(@RequestParam orderId: String,
                    @RequestParam deliveryPersonId: String): ResponseEntity<*>? {
        val response = restaurantService.selectOrder(orderId, deliveryPersonId)
        return ResponseEntity<String>(response, HttpStatus.OK)
    }



    @PostMapping("/delivery/partner/register")
    fun registerDeliveryPartner(@RequestBody deliveryPerson: DeliveryPerson): ResponseEntity<*>? {
        val response = restaurantService.registerDeliveryPerson(deliveryPerson)
        return ResponseEntity<String>(response, HttpStatus.OK)
    }

    @GetMapping("/delivery/partner/login")
    fun loginDeliveryPartner(@RequestBody loginRequest: LoginRequest): ResponseEntity<*>? {
        val response = restaurantService.loginDeliveryPerson(loginRequest)
        return ResponseEntity<LoginResponse>(response, HttpStatus.OK)
    }

    //Admin

    @DeleteMapping("/admin/restaurant")
    fun deleteRestById(@RequestParam(value = "id") restId: String?,
    @RequestParam(value = "userRole") userRole: String) : ResponseEntity<*>? {
        if(userRole == "admin") {
            restaurantService.deleteRest(restId)
            return ResponseEntity<String>("Success! Restaurant $restId deleted", HttpStatus.OK)
        } else {
            return ResponseEntity<String>("User doesnot have permission to access this API", HttpStatus.FORBIDDEN)
        }

    }

    @DeleteMapping("/admin/delivery/partner")
    fun deleteDeliveryPartner(@RequestParam("id") deliveryPersonId: String,
                              @RequestParam(value = "userRole") userRole: String): ResponseEntity<*>? {
        if(userRole == "admin") {
            restaurantService.deleteDeliveryPerson(deliveryPersonId)
            return ResponseEntity<String>("Success! Delivery Person $deliveryPersonId deleted", HttpStatus.OK)
        }else {
            return ResponseEntity<String>("User doesnot have permission to access this API", HttpStatus.FORBIDDEN)
        }
    }

    @GetMapping("/admin/list/allOrders")
    fun listAllOrders( @RequestParam(value = "userRole") userRole: String): ResponseEntity<*>? {
        if(userRole == "admin") {
          val response =  restaurantService.getAllStatusOrders()
            return ResponseEntity<List<Orders>>(response, HttpStatus.OK)
        }else {
            return ResponseEntity<String>("User doesnot have permission to access this API", HttpStatus.FORBIDDEN)
        }
    }

    @GetMapping("/admin/active/orders")
    fun listActiveOrders( @RequestParam(value = "userRole") userRole: String): ResponseEntity<*>? {
        if(userRole == "admin") {
            val response =  restaurantService.listActiveOrders()
            return ResponseEntity<Map<String, Int>>(response, HttpStatus.OK)
        }else {
            return ResponseEntity<String>("User doesnot have permission to access this API", HttpStatus.FORBIDDEN)
        }
    }

    @GetMapping("/admin/list/allRestaurants")
    fun getAdminAllRest() : ResponseEntity<*>? {
        val rests: List<Restaurant?> = restaurantService.getAllRest()
        return ResponseEntity<List<Restaurant?>>(rests, HttpStatus.OK)
    }

    @GetMapping("/admin/list/allRestaurants/food-items")
    fun getAllRestMenu() : ResponseEntity<*>? {
        val rests: List<Restaurant?> = restaurantService.getAllRestsMenu()
        return ResponseEntity<List<Restaurant?>>(rests, HttpStatus.OK)
    }
}
