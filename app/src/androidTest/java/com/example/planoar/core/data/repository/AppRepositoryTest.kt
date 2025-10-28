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

    // --- SOLO CORREMOS ESTE TEST ---
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

    // --- DESACTIVAMOS ESTE TEST TEMPORALMENTE ---
//    @Ignore("Desactivado hasta resolver el problema del TypeConverter") // <-- Desactiva el test
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