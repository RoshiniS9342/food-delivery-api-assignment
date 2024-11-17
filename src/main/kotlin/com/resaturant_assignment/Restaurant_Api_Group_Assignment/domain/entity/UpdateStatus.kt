package com.resaturant_assignment.Restaurant_Api_Group_Assignment.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateStatus (
    @JsonProperty("status")
    val status: String?
)