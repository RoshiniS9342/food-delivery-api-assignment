package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.*
import java.util.*

interface RestaurantService {
    fun addRest(restaurant: Restaurant): Restaurant

    fun getAllRest(): List<Restaurant?>

    fun getRestById(id: String?):Restaurant?

    fun addFoodItem(foodItem: FoodItem, id: String?):  Optional<Restaurant>?

     fun getRestMenuById(id: String?): Restaurant?

     fun getAllRestsMenu(): List<Restaurant?>

     fun deleteRest(id: String?)

    fun deleteFoodById(restId: String?,foodId: String?) : Restaurant?

    fun updateOrder(orderDetails: Orders, restId: String?): String

    fun getOrderById( orderId: String): Orders?

    fun updateOrderStatus(orderId: String, newStatus: UpdateStatus): String

    fun getOrderStatus(orderId: String): UpdateStatus

    fun registerManager(restaurantId: String, manager: Manager): String

    fun login(loginRequest: LoginRequest, resId: String): LoginResponse

    fun getAllOrders(): List<Orders>

    fun selectOrder(orderId: String, deliveryPersonId: String): String

    fun registerDeliveryPerson(deliveryPerson: DeliveryPerson) : String

    fun loginDeliveryPerson(loginRequest: LoginRequest) : LoginResponse

     fun deleteDeliveryPerson(deliveryPersonId: String)

    fun getAllStatusOrders(): List<Orders>

    fun listActiveOrders(): Map<String, Int>

    fun deleteOrderById(orderId: String): String

}