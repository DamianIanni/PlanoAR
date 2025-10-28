package com.example.planoar.core.domain.repository

import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.model.Room
import kotlinx.coroutines.flow.Flow

/**
 * Este es el "contrato" que la capa de Dominio (UseCases)
 * espera que la capa de Datos (Data) implemente.
 */
interface AppRepository {

    // --- Funciones de House ---
    fun getAllHouses(): Flow<List<House>>
    suspend fun insertHouse(house: House)
    suspend fun deleteHouse(house: House)

    // --- Funciones de Room ---
    fun getRoomsForHouse(houseId: Int): Flow<List<Room>>
    suspend fun insertRoom(room: Room)
    suspend fun deleteRoom(room: Room)
}