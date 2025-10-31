package com.example.planoar.core.domain.usecase.houseUseCases

import com.example.planoar.FakeAppRepository // Importa tu Fake
import com.example.planoar.core.domain.model.House
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class InsertHouseUseCaseTest {

    private lateinit var fakeRepository: FakeAppRepository
    private lateinit var insertHouseUseCase: InsertHouseUseCase

    @Before
    fun setup() {
        fakeRepository = FakeAppRepository()
        // La clase InsertHouseUseCase aún no existe o está vacía,
        // pero la instanciamos para que el test compile (ROJO)
        insertHouseUseCase = InsertHouseUseCase(repository = fakeRepository)
    }

    @After
    fun tearDown() {
        fakeRepository.reset()
    }

    @Test
    fun `dado nombre valido, invoke crea casa y llama repository_insertHouse`() = runTest {
        // Arrange
        val houseName = "Casa Nueva"
        // Esperamos que cree una casa con ID 0 (Room lo autogenera) y el nombre limpio
        val expectedHouseToInsert = House(id = 0, name = houseName.trim())

        // Act
        insertHouseUseCase(houseName) // Llamamos al UseCase

        // Assert
        // Verificamos que se llamó a la función correcta del Fake
        assertThat(fakeRepository.insertHouseCalled).isTrue()
        // Verificamos que los datos pasados al Fake son los correctos
        assertThat(fakeRepository.lastInsertedHouse).isEqualTo(expectedHouseToInsert)
    }

//    @Test
//    fun `dado nombre duplicado, invoke NO llama repository_insertHouse`() = runTest {
//        // Arrange
//        val existingName = "Casa Existente"
//        fakeRepository.addExistingHouseName(existingName) // Pre-cargamos el nombre en el Fake
//
//        // Act
//        insertHouseUseCase(existingName) // Intentamos insertar el duplicado
//
//        // Assert
//        // Verificamos que NUNCA se llamó a la función de inserción del Fake
//        assertThat(fakeRepository.insertHouseCalled).isFalse()
//        assertThat(fakeRepository.lastInsertedHouse).isNull()
//        // Verificamos que SÍ se llamó a la comprobación de existencia
//        assertThat(fakeRepository.doesHouseExistCalledWith).isEqualTo(existingName)
//    }

    @Test(expected = IllegalArgumentException::class) // Esperamos que lance esta excepción
    fun `dado nombre vacío, invoke lanza IllegalArgumentException y no llama al repo`() = runTest {
        // Arrange
        val emptyName = "   " // Nombre vacío o solo espacios

        // Act
        try {
            insertHouseUseCase(emptyName) // Esto debería lanzar la excepción
        } finally {
            // Assert (se ejecuta incluso si hay excepción)
            // Verificamos que NUNCA se llamó a insertar ni a comprobar existencia
            assertThat(fakeRepository.insertHouseCalled).isFalse()
            assertThat(fakeRepository.doesHouseExistCalledWith).isNull()
        }
    }
}