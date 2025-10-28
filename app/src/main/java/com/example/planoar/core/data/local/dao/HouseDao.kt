package com.example.planoar.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.planoar.core.data.local.entities.HouseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
    // 'suspend' porque es una operación de una sola vez (escribir)
    // OnConflictStrategy.REPLACE: Si inserto una casa con un ID que ya existe,
    // la reemplaza (útil para "actualizar").
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(house: HouseEntity)

    @Delete
    suspend fun deleteHouse(house: HouseEntity)

    // 'Flow' es un "flujo" de datos. Room lo usará para
    // notificarnos automáticamente cada vez que la tabla "houses" cambie.
    // Esto es la base de la UI reactiva.
    @Query("SELECT * FROM houses ORDER BY name ASC")
    fun getAllHouses(): Flow<List<HouseEntity>>
}