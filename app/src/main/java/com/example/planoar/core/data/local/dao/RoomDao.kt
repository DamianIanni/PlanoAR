package com.example.planoar.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.planoar.core.data.local.entities.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Delete
    suspend fun deleteRoom(room: RoomEntity)

    // Obtenemos solo las habitaciones que pertenecen a una casa específica
    @Query("SELECT * FROM rooms WHERE houseId = :houseId ORDER BY name ASC")
    fun getRoomsForHouse(houseId: Int): Flow<List<RoomEntity>>
}