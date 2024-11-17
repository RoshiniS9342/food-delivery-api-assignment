package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.mongodb.core.mapping.Document

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "delivery_partner_db")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class DeliveryPerson (
    val id : String?,
    val name: String?,
    val phoneNumber: String?,
    val username: String?,
    var password: String?
)
