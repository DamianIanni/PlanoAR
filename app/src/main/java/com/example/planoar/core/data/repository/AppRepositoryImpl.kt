package com.example.planoar.core.data.repository

import com.example.planoar.core.data.local.dao.HouseDao
import com.example.planoar.core.data.local.dao.RoomDao
import com.example.planoar.core.data.local.entities.HouseEntity
import com.example.planoar.core.data.local.entities.RoomEntity
import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.model.Room
import com.example.planoar.core.domain.repository.AppRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Esta es la implementación "real" del repositorio.
 * Hilt inyectará los DAOs automáticamente en el constructor.
 */
class AppRepositoryImpl @Inject constructor(
    private val houseDao: HouseDao,
    private val roomDao: RoomDao,
    private val gson: Gson // También pedimos Gson (que Hilt proveerá)
) : AppRepository {

    // --- Implementación de House ---

    override fun getAllHouses(): Flow<List<House>> {
        // El DAO devuelve Flow<List<HouseEntity>>
        // Usamos .map para "traducir" la lista de Entity a Model
        return houseDao.getAllHouses().map { entityList ->
            entityList.map { it.toDomainModel() } // Traducimos cada item
        }
    }

    override suspend fun insertHouse(house: House) {
        // Traducimos el Model de Dominio a una Entity de DB antes de guardarlo
        houseDao.insertHouse(house.toDataEntity())
    }

    override suspend fun deleteHouse(house: House) {
        houseDao.deleteHouse(house.toDataEntity())
    }

    override suspend fun updateHouse(house: House) {
        houseDao.updateHouse(house.toDataEntity())
    }

    override suspend fun doesHouseExist(name: String): Boolean {
      return houseDao.doesHouseExist(name)
    }

    // --- Implementación de Room ---

    override fun getRoomsForHouse(houseId: Int): Flow<List<Room>> {
        return roomDao.getRoomsForHouse(houseId).map { entityList ->
            entityList.map { it.toDomainModel(gson) }
        }
    }

    override suspend fun insertRoom(room: Room) {
        roomDao.insertRoom(room.toDataEntity(gson))
    }

    override suspend fun deleteRoom(room: Room) {
        roomDao.deleteRoom(room.toDataEntity(gson))
    }

    override suspend fun updateRoom(room: Room) {
        TODO("Not yet implemented")
    }

    override suspend fun doesRoomExist(
        name: String,
        houseId: Int
    ): Boolean {
       return roomDao.doesRoomExist(name, houseId)
    }
}


// --- FUNCIONES DE "TRADUCCIÓN" (Mappers) ---
// (Puedes poner esto en su propio archivo "Mappers.kt" si quieres,
// pero aquí está bien por ahora)

private fun HouseEntity.toDomainModel(): House {
    return House(
        id = this.id,
        name = this.name
    )
}

private fun House.toDataEntity(): HouseEntity {
    return HouseEntity(
        id = this.id,
        name = this.name
    )
}

private fun RoomEntity.toDomainModel(gson: Gson): Room {
    val listType = object : TypeToken<List<com.example.planoar.core.domain.model.DomainPoint>>() {}.type
    return Room(
        id = this.id,
        houseId = this.houseId,
        name = this.name,
        area = this.area,
        points = gson.fromJson(this.points, listType) // Traducir de JSON
    )
}

private fun Room.toDataEntity(gson: Gson): RoomEntity {
    return RoomEntity(
        id = this.id,
        houseId = this.houseId,
        name = this.name,
        area = this.area,
        points = gson.toJson(this.points) // Traducir a JSON
    )
}