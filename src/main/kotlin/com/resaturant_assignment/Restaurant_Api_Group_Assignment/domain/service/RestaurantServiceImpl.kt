package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.service

import com.resaturant_assignment.Restaurant_Api_Group_Assignment.config.JwtUtil
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity.*
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository.DeliveryRepo
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository.OrderRepo
import com.resaturant_assignment.Restaurant_Api_Group_Assignment.repository.RestRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class RestaurantServiceImpl(private val restRepo: RestRepo,
    private val orderRepo: OrderRepo,
    private val deliveryRepo: DeliveryRepo,
    private val passwordEncoder: PasswordEncoder) : RestaurantService {


    @Autowired
    private lateinit var jwtUtil: JwtUtil

    override fun addRest(restaurant: Restaurant): Restaurant{
        return restRepo.save(restaurant)
    }

    override fun getAllRest(): List<Restaurant?> {
        val restaurantsCards = restRepo.findAll().toList()
        val restaurantList = mutableListOf<Restaurant>()
        restaurantsCards.forEach { rest ->
            restaurantList.add(
                Restaurant(id = rest.id, name = rest.name, address = rest.address, phoneNumber = rest.phoneNumber)
            )
        }
        return restaurantList
    }

    override fun getRestById(id: String?): Restaurant? {
        var resturant =   id?.let { restRepo.findById(it).get() }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant Id is mandatory")

       resturant.manager = null
        return resturant
    }

    override fun addFoodItem(foodItem: FoodItem, id: String?): Optional<Restaurant>? {
      val restaurant = id?.let { restRepo.findById(it) }
       val savedRest = restaurant?.map{ rest ->
          var foodItemList : MutableList<FoodItem> = mutableListOf()
         if(rest.foodItems.isNullOrEmpty()){
             foodItemList.add(foodItem)
         } else {
             foodItemList = rest?.foodItems?.toMutableList()!!
             foodItemList.add(foodItem)
         }
           rest.foodItems = foodItemList
           restRepo.save(rest)
        }
        return savedRest
    }

    override fun getRestMenuById(id: String?): Restaurant?{
        val restaurant = id?.let { restRepo.findById(it) }
        var restaurantMenu: Restaurant? = null
         restaurant?.map { rest ->
              restaurantMenu =  Restaurant(id = rest.id, name = rest.name, foodItems = rest.foodItems)
        } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant Does not exist")
        return restaurantMenu
    }

    override fun getAllRestsMenu(): List<Restaurant?> {
        val restaurantsCards = restRepo.findAll().toList()
        val restaurantList = mutableListOf<Restaurant>()
        restaurantsCards.forEach { rest ->
            restaurantList.add(
                Restaurant(id = rest.id, name = rest.name, foodItems = rest.foodItems)
            )
        }
        return restaurantList
    }
   override fun deleteRest(id: String?) {
      id?.let { restRepo.deleteById(it)}
          ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant Id is mandatory")
    }

   override fun deleteFoodById(restId: String?,foodId: String?) : Restaurant?{
        val restaurant = restId?.let { restRepo.findById(it).get()}
            val foodList =  restaurant?.foodItems!!.toMutableList()
           val restaurantItem = restaurant.foodItems?.find { it.foodId == foodId }
           foodList.remove(restaurantItem)
       restaurant.foodItems = foodList
            restRepo.save(restaurant)

        return restaurant
    }

    override fun updateOrder(orderDetails: Orders, restId: String?): String {
        var result = ""
        val rest= restId?.let { restRepo.findById(it) }
        //Initialize order list if null
        rest?.map { restaurant ->
            if(restaurant == null) {
                result = "No restaurant found with this Id"
            }

            // Verify food items availability and quantity
            val unavailableItems = mutableListOf<String>()
            val insufficientQuantityItems = mutableListOf<String>()

            orderDetails.foodItems?.forEach { orderItem ->
                val restaurantItem = restaurant.foodItems?.find { it.foodId == orderItem.foodId }

                if (restaurantItem == null) {
                    unavailableItems.add(orderItem.foodName ?: "Unknown Item")
                } else if ((restaurantItem.quantity ?: 0) < (orderItem.quantity ?: 0)) {
                    insufficientQuantityItems.add(orderItem.foodName ?: "Unknown Item")
                }
            }

            // Check if there are any errors with the items
            if (unavailableItems.isNotEmpty()) {
                result = "Order failed. The following items are unavailable: ${unavailableItems.joinToString(", ")}"
            }
            if (insufficientQuantityItems.isNotEmpty()) {
                result = "Order failed. Insufficient quantity for the following items: ${
                    insufficientQuantityItems.joinToString(
                        ", "
                    )
                }"
            }

            // Update quantities for each food item in the restaurant's inventory
            orderDetails.foodItems?.forEach { orderItem ->
                val restaurantItem = restaurant.foodItems?.find { it.foodId == orderItem.foodId }
                if (restaurantItem != null) {
                    restaurantItem.quantity = (restaurantItem.quantity ?: 0) - (orderItem.quantity ?: 0)
                }
            }
            orderDetails.orderStatus = "Order Received"
            orderDetails.restaurantName = restaurant.name

            orderRepo.save(orderDetails)
            result = "Order Placed successfully."
        }
        return result

    }

   override fun updateOrderStatus(orderId: String, newStatus: UpdateStatus): String {
        var result = ""
       val order = orderRepo.findById(orderId)
       order.map { od ->
           if(od == null) {
               result = "No orders found with this Id"
           }
           od.orderStatus = newStatus.status
           orderRepo.save(od)
           result = "Order status updated to '$newStatus' for order ID $orderId."
       }


        return result
    }

    override fun getOrderById(orderId: String): Orders? {
        var orderDetails: Orders? = null
        // Check if the restaurant has orders

        val order = orderRepo.findById(orderId)
        order.map { od ->
            orderDetails = od

        }
        return orderDetails
    }

    override fun getAllOrders(): List<Orders> {
       val orders = orderRepo.findAll().toList()
        val orderList :MutableList<Orders> = mutableListOf()
        orders.forEach { order ->
            if(order.orderStatus == "Preparing Order, Looking for Delivery Partner")
                orderList.add(order)

        }
        return orderList
    }

    override fun registerManager(restaurantId: String, manager: Manager): String {
        // Find the restaurant by ID
        val restaurant = restRepo.findById(restaurantId).get()
           if(restaurant == null) {
               throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant with ID $restaurantId not found")
           }
        if(restaurant.manager == null) {
            restaurant.manager = Manager(username = manager.username, password = passwordEncoder.encode(manager.password))
            restRepo.save(restaurant)
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate Entry: Restaurant with ID $restaurantId already has manager")
        }

        return "Manager registered successfully for restaurant ${restaurant.name}."
    }

    override fun login(loginRequest: LoginRequest, resId: String): LoginResponse {
        // Find the restaurant by manager's username
        val restaurant = restRepo.findById(resId).get()
        var token : String? = null
           if (!passwordEncoder.matches(loginRequest.password,restaurant.manager?.password)) {
               throw ResponseStatusException(HttpStatus.BAD_REQUEST, "password incorrect")
           }
        token= restaurant.manager?.username?.let { jwtUtil.generateToken(it) }
        return LoginResponse(token)
    }

    override fun selectOrder(orderId: String, deliveryPersonId: String): String{
        val deliveryPerson = deliveryRepo.findById(deliveryPersonId).get()
        val order = orderRepo.findById(orderId).get()
        order.deliveryPerson = DeliveryPerson(id = deliveryPersonId, name = deliveryPerson.name, phoneNumber = deliveryPerson.phoneNumber, null, null)
        order.orderStatus = "Assigned Delivery Partner ${deliveryPerson.name}"
        orderRepo.save(order)
        return "Confirmed the selection of Order $orderId"
    }

    override fun getOrderStatus(orderId: String): UpdateStatus {
        val order = orderRepo.findById(orderId).get()
        return UpdateStatus(status = order.orderStatus)
    }

    override fun registerDeliveryPerson(deliveryPerson: DeliveryPerson) : String{
        deliveryPerson.password = passwordEncoder.encode(deliveryPerson.password)
         deliveryRepo.save(deliveryPerson)
        return "Delivery Partner ${deliveryPerson.name} successfully registered"
    }

    override fun loginDeliveryPerson(loginRequest: LoginRequest) : LoginResponse{

        val deliveryPerson = deliveryRepo.findAll().find { it.username == loginRequest.username }

        if (!passwordEncoder.matches(loginRequest.password,deliveryPerson?.password)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "password incorrect")
        }
        val token= deliveryPerson?.password?.let { jwtUtil.generateToken(it) }
        return LoginResponse(token)
    }

   override fun deleteDeliveryPerson(deliveryPersonId: String) {
        deliveryRepo.deleteById(deliveryPersonId)
    }

    override fun getAllStatusOrders(): List<Orders> {
        return orderRepo.findAll().toList()
    }

    override fun listActiveOrders(): Map<String, Int> {
        val orderList = orderRepo.findAll().toList()
        val restList = restRepo.findAll().toList()
        val activeOrders: MutableList<Orders> = mutableListOf()
        val activeOrdersMap = mutableMapOf<String, Int>()
        orderList.forEach { order ->
            if (order.orderStatus != "Order Delivered") {
                activeOrders.add(
                    Orders(
                        orderId = order.orderId,
                        orderStatus = order.orderStatus,
                        restaurantName = order.restaurantName,
                        customerName = order.customerName,
                        deliveryAddress = order.deliveryAddress,
                        paymentMethod = null
                    )
                )
            }
        }
        for (restaurant in restList) {
            val restOrderList = orderList.filter { it.restaurantName == restaurant.name }
            activeOrdersMap[restaurant.name ?: "Unknown"] = restOrderList.size
        }
        return activeOrdersMap
    }

    override fun deleteOrderById(orderId: String): String {
        val order = orderRepo.findById(orderId).get()
        val result = if (order.orderStatus == "Order Pending") {
            orderRepo.deleteById(orderId)
            "Order $orderId successfully deleted"
        } else {
            "Order $orderId can not be cancelled, Restaurant has started preparing the order"
        }
        return result
    }



}