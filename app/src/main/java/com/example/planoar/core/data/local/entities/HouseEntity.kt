package com.example.planoar.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "houses")
data class HouseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)
