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
@Document(collection = "customer_db")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Customer(
    @Id // MongoDB will automatically generate this ID
    val id: String? = null, // MongoDB uses ObjectId which is typically a String
    val name: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    val paymentType: String? = null,
    var password: String? = null,
    val username: String? = null
)