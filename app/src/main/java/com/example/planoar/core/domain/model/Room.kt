package com.example.planoar.core.domain.model

data class Room(
    val id: Int,
    val houseId: Int,
    val name: String,
    val points: List<DomainPoint>,
    val area: Double = 0.0
)
