package com.example.planoar.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = HouseEntity::class,
            parentColumns = ["id"], // Columna en la tabla "houses"
            childColumns = ["houseId"], // Columna en esta tabla ("rooms")
            onDelete = ForeignKey.CASCADE // Si se borra la casa, se borran sus habitaciones
        )
    ]
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val houseId: Int,
    val name: String,
    val area: Double,
    val points: String
)
