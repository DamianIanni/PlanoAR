package com.example.planoar

import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.model.Room
import com.example.planoar.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Una implementación FALSA del AppRepository para usar en tests unitarios.
 * Guarda los datos en memoria.
 */
class FakeAppRepository: AppRepository {
    // Usamos StateFlow para simular el Flow de Room
    private val _housesFlow = MutableStateFlow<List<House>>(emptyList())
    private val _roomsFlow = MutableStateFlow<List<Room>>(emptyList())

    var insertHouseCalled = false
        private set // Solo el Fake puede cambiarla
    var lastInsertedHouse: House? = null
        private set

    var insertRoomCalled = false
        private set
    var lastInsertedRoom: Room? = null
        private set
    var deleteHouseCalledWith: House? = null
        private set

    var updateHouseCalledWith: House? = null
        private set

    var updateRoomCalledWith: Room? = null
        private set

    var doesHouseExistCalledWith: String? = null // Para verificar la llamada a doesHouseExist
    var doesRoomExistCalledWith: String? = null // Para verificar la llamada a doesRoomExist

    // --- Simulación de datos existentes ---
    private var existingHouseNames = mutableSetOf<String>() // Guardamos nombres existentes (en minúscula)
    private var existingRoomNames = mutableSetOf<String>() // Guardamos nombres existentes (en minúscula)

    // --- Implementación de House ---

    override fun getAllHouses(): Flow<List<House>> {
        return _housesFlow.asStateFlow() // Devolvemos el Flow observable
    }

    override suspend fun insertHouse(house: House) {
        insertHouseCalled = true
        lastInsertedHouse = house
        existingHouseNames.add(house.name.lowercase())
        // Simulamos la inserción añadiéndolo a la lista del Flow
        _housesFlow.update { currentList -> currentList + house }
    }

    override suspend fun deleteHouse(house: House) {
        deleteHouseCalledWith = house
        // Implementación simple para borrar (si la necesitaras)
        _housesFlow.update { currentList -> currentList.filterNot { it.id == house.id } }
    }

    override suspend fun updateHouse(house: House) {
        // Simple update simulation
        updateHouseCalledWith = house
        _housesFlow.update { currentList ->
            currentList.map { if (it.id == house.id) house else it }
        }
    }

    override suspend fun doesHouseExist(name: String): Boolean {
        doesHouseExistCalledWith = name // Guardamos con qué nombre se llamó
        return existingHouseNames.contains(name.lowercase())
    }

    // --- Implementación de Room ---

    override fun getRoomsForHouse(houseId: Int): Flow<List<Room>> {
        // En un Fake simple, podemos devolver todas las rooms o filtrar
        return _roomsFlow.map { roomList -> roomList.filter { it.houseId == houseId}} // Mejor filtrar
    }

    override suspend fun insertRoom(room: Room) {
        insertRoomCalled = true
        lastInsertedRoom = room
        existingRoomNames.add(room.name.lowercase())
        _roomsFlow.update { currentList -> currentList + room }
    }

    override suspend fun deleteRoom(room: Room) {
        _roomsFlow.update { currentList -> currentList.filterNot { it.id == room.id } }
    }

    override suspend fun updateRoom(room: Room) {
        updateRoomCalledWith = room
        _roomsFlow.update { currentList ->
            currentList.map { if (it.id == room.id) room else it }
        }
    }

    override suspend fun doesRoomExist(name: String, houseId: Int): Boolean {
        return existingRoomNames.contains(name.lowercase())
    }

    // Función helper para limpiar el estado entre tests
    fun reset() {
        _housesFlow.value = emptyList()
        _roomsFlow.value = emptyList()
        insertHouseCalled = false
        lastInsertedHouse = null
        insertRoomCalled = false
        lastInsertedRoom = null
        deleteHouseCalledWith = null
        updateHouseCalledWith = null
        doesRoomExistCalledWith = null
        doesHouseExistCalledWith = null
        existingHouseNames = mutableSetOf()
        existingRoomNames = mutableSetOf()
    }
}