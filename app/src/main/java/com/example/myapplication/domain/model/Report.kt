package com.example.myapplication.domain.model

data class Report(
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: Category,
    val status: Status = Status.PENDIENTE,
    val photoUri: String,
    val latitude: Double,
    val longitude: Double,
    val date: String
)