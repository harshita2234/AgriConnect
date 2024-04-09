package com.example.agriconnect.models

data class Listing(
    val id: String = "",
    val phoneNumber: String = "",
    val role: String = "",
    val crop: String = "",
    val quantity: String = "",
    val location: String = "",
    val price: String = "",
    val transactionId: String = "",
    val merchantPhoneNumber: String = ""
)