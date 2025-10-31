package com.example.planoar.core.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
// Importamos la clase Room de la librería explícitamente
//import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.planoar.core.data.local.AppDatabase
import com.example.planoar.core.data.local.converters.Converters // Todavía lo necesitamos para AppRepositoryImpl
import com.example.planoar.core.data.local.dao.HouseDao
import com.example.planoar.core.data.local.dao.RoomDao
// Aquí importamos TU modelo Room
import com.example.planoar.core.domain.model.Room
import com.example.planoar.core.domain.model.DomainPoint
import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.repository.AppRepository
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Ignore // <-- Import Ignore para desactivar tests
import org.junit.Rule
import org.junit.Test
import java.lang.Exception // Para el try-catch

@SmallTest
class AppRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var houseDao: HouseDao
    private lateinit var roomDao: RoomDao
    private lateinit var gson: Gson
    private lateinit var repository: AppRepository
    private lateinit var converters: Converters

    @Before
    fun setup() {
        println(">>> AppRepositoryTest: @Before setup() - INICIO")
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            println(">>> AppRepositoryTest: Contexto obtenido.")
            gson = Gson()
            converters = Converters()

            database = androidx.room.Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
            )
                // --- ¡CAMBIO AQUÍ! Comentamos el TypeConverter ---
//                 .addTypeConverter(converters)
                // --------------------------------------------------
                .allowMainThreadQueries()
                .build()

            println(">>> AppRepositoryTest: Base de datos inicializada: ${::database.isInitialized}")

            houseDao = database.houseDao()
            println(">>> AppRepositoryTest: HouseDao inicializado.") // Quitamos isInitialized por simplicidad
            roomDao = database.roomDao()
            println(">>> AppRepositoryTest: RoomDao inicializado.") // Quitamos isInitialized
            gson = Gson()
            println(">>> AppRepositoryTest: Gson inicializado.")

            repository = AppRepositoryImpl(
                houseDao = houseDao,
                roomDao = roomDao,
                gson = gson
            )
            println(">>> AppRepositoryTest: CHECK INMEDIATO - Repo inicializado?: ${::repository.isInitialized}")
            println(">>> AppRepositoryTest: Repositorio inicializado.") // Quitamos isInitialized

        } catch (e: Exception) {
            println(">>> AppRepositoryTest: ERROR EN SETUP: ${e.message}")
            e.printStackTrace()
        }
        println(">>> AppRepositoryTest: @Before setup() - FIN")
    }

    @After
    fun tearDown() {
        println(">>> AppRepositoryTest: @After tearDown() - INICIO")
        println(">>> AppRepositoryTest: ¿Database inicializada?: ${if(this::database.isInitialized) "SI" else "NO"}")
        if (::database.isInitialized) {
            database.close()
            println(">>> AppRepositoryTest: Base de datos cerrada.")
        } else {
            println(">>> AppRepositoryTest: Base de datos NO estaba inicializada en tearDown.")
        }
        println(">>> AppRepositoryTest: @After tearDown() - FIN")
    }

    // HOUSE TEST
    @Test
    fun insertHouse_getAllHouses_returnsSameHouse() = runTest {
        println(">>> AppRepositoryTest: Test insertHouse - INICIO")
        // Asegurarnos que el repo se inicializó (si setup falla, esto fallará aquí)
        if (!::repository.isInitialized) {
            throw IllegalStateException("Repositorio no inicializado en el test de House")
        }
        val house = House(id = 1, name = "Casa de Prueba")
        repository.insertHouse(house)
        val houses = repository.getAllHouses().first()
        assertThat(houses).isNotEmpty()
        assertThat(houses.first().name).isEqualTo("Casa de Prueba")
        println(">>> AppRepositoryTest: Test insertHouse - FIN")
    }

    @Test
    fun deleteHouse_getAllHouses_removesHouse() = runTest {
        println(">>> AppRepositoryTest: Test deleteHouse - INICIO")
        // Asegurarnos que el repo se inicializó (si setup falla, esto fallará aquí)
        if (!::repository.isInitialized) {
            throw IllegalStateException("Repositorio no inicializado en el test de House")
        }
        val house1 = House(id = 1, name = "Casa de Prueba")
        val house2 = House(id = 3, name = "Casa de")
        repository.insertHouse(house1)
        repository.insertHouse(house2)
        val allHousesBeforeDelete = repository.getAllHouses().first()
        assertThat(allHousesBeforeDelete).contains(house1)
        repository.deleteHouse(house1)
        val housesAfterDelete = repository.getAllHouses().first()
        assertThat(house1).isNotIn(housesAfterDelete)
        println(">>> AppRepositoryTest: Test insertHouse - FIN")
    }

    @Test
    fun updateHouse_getAllHouses_returnsUpdatedHouse() = runTest {
        println(">>> AppRepositoryTest: Test deleteHouse - INICIO")
        // Asegurarnos que el repo se inicializó (si setup falla, esto fallará aquí)
        if (!::repository.isInitialized) {
            throw IllegalStateException("Repositorio no inicializado en el test de House")
        }
        val house1 = House(id = 1, name = "Casa de Prueba")
        val house1Updated = House(id = 1, name = "Casa de Test")
        repository.insertHouse(house1)
        val houseBeforeUpdate = repository.getAllHouses().first()
        assertThat(houseBeforeUpdate.first().name).isEqualTo("Casa de Prueba")
        repository.updateHouse(house1Updated)
        val housesAfterUpdate = repository.getAllHouses().first()
        assertThat(housesAfterUpdate.first().name).isEqualTo("Casa de Test")
        println(">>> AppRepositoryTest: Test insertHouse - FIN")
    }

    @Test
    fun doesHouseExist_returnsTrue_whenHouseExists() = runTest {
        println(">>> AppRepositoryTest: Test doesExist TRUE - INICIO")
        if (!::repository.isInitialized) {
            throw IllegalStateException("Repositorio no inicializado en el test de DoesExist True")
        }

        // --- Arrange ---
        // 1. Insertamos la casa que vamos a buscar
        val houseName = "Casa Existente"
        val house = House(id = 0, name = houseName)
        repository.insertHouse(house)

        // --- Act ---
        // 2. Llamamos a la función para verificar si existe
        val exists = repository.doesHouseExist(houseName)

        // --- Assert ---
        // 3. Verificamos que devuelva TRUE
        assertThat(exists).isTrue()

        println(">>> AppRepositoryTest: Test doesExist TRUE - FIN")
    }

    @Test
    fun doesHouseExist_returnsTrue_whenHouseDoesNotExists() = runTest {
        println(">>> AppRepositoryTest: Test doesExist FALSE - INICIO")
        if (!::repository.isInitialized) {
            throw IllegalStateException("Repositorio no inicializado en el test de DoesExist False")
        }

        // --- Arrange ---
        // 1. Nos aseguramos de que la base de datos esté vacía (el @Before y @After lo hacen)
        //    Opcionalmente, puedes insertar una casa con OTRO nombre para estar seguro.
        //    val otherHouse = House(id = 0, name = "Otra Casa")
        //    repository.insertHouse(otherHouse)

        val nameToSearch = "Casa Inexistente"

        // --- Act ---
        // 2. Llamamos a la función con un nombre que NO existe
        val exists = repository.doesHouseExist(nameToSearch)

        // --- Assert ---
        // 3. Verificamos que devuelva FALSE
        assertThat(exists).isFalse()

        println(">>> AppRepositoryTest: Test doesExist FALSE - FIN")
    }

    @Test
    fun insertRoom_getRoomsForHouse_returnsRoomWithCorrectPoints() = runTest {
        println(">>> AppRepositoryTest: Test insertRoom - INICIO")
        // Asegurarnos que el repo se inicializó
        if (!::repository.isInitialized) {
            throw IllegalStateException("Repositorio no inicializado en el test de Room")
        }
        val house = House(id = 0, name = "Casa")
        repository.insertHouse(house)
        val insertedHouse = repository.getAllHouses().first().first()
        val realHouseId = insertedHouse.id

        val testPoints = listOf(DomainPoint(1f,1f,1f), DomainPoint(2f,2f,2f))
        val room = Room(
            id = 0,
            houseId = realHouseId,
            name = "Cocina",
            area = 15.0,
            points = testPoints
        )

        repository.insertRoom(room)
        val rooms = repository.getRoomsForHouse(houseId = realHouseId).first()

        assertThat(rooms).isNotEmpty()
        assertThat(rooms.first().name).isEqualTo("Cocina")
        assertThat(rooms.first().points).isEqualTo(testPoints)
        println(">>> AppRepositoryTest: Test insertRoom - FIN")
    }
}