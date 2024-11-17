package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "restaurant_db")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Restaurant (
    @Id
    val id: String?,
    val name : String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    var manager: Manager? = null,
    var foodItems: List<FoodItem>? = null,
//    var orderDetails: List<OrderDetails>? = null
)

data class Manager(
    val username : String?,
    val password: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FoodItem(
    val foodId: String?,
    val foodName: String?,
    val price: Double? = null,
    var quantity: Int? = null,
    val type: String? = null,
    val status: String? = null,
)

//@JsonInclude(JsonInclude.Include.NON_NULL)
//data class OrderDetails(
//    val orderId: String?,
//    val foodItems: List<FoodItem>? = null,
//    val totalAmount : Double? = null,
//    val customerName: String? = null,
//    val deliveryAddress: String?,
//    val paymentMethod: String?,
//    var orderStatus: String? = null,
//)