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
@Document(collection = "order_db")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Orders (
    @Id
    val orderId: String?,
    val foodItems: List<FoodItem>? = null,
    val totalAmount : Double? = null,
    var restaurantName: String? = null,
    val customerId: String? = null,
    val customerName: String? = null,
    val customerPhoneNo: String? = null,
    val deliveryAddress: String?,
    val paymentMethod: String?,
    var orderStatus: String? = null,
    var deliveryPerson: DeliveryPerson? = null
)

