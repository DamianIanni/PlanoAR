package com.example.planoar.core.domain.usecase.houseUseCases

import com.example.planoar.FakeAppRepository
import com.example.planoar.core.domain.model.House
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UpdateHouseUseCaseTest {
    private lateinit var fakeAppRepository: FakeAppRepository
    private lateinit var updateHouseUseCase: UpdateHouseUseCase

    @Before
    fun setup() {
        fakeAppRepository = FakeAppRepository()
        updateHouseUseCase = UpdateHouseUseCase(fakeAppRepository)
    }

    @After
    fun tearDown() {
        fakeAppRepository.reset()
    }

    @Test
    fun `Given a House to update, When the use case is executed, Then the house is updated`() = runTest{
        val houseToUpdate = House(id = 1, name = "España")
        val houseUpdated = House(id = 1, name = "Italia")
        fakeAppRepository.insertHouse(houseToUpdate)
        assertThat(fakeAppRepository.lastInsertedHouse).isEqualTo(houseToUpdate)
        updateHouseUseCase(houseUpdated)
        assertThat(fakeAppRepository.updateHouseCalledWith).isEqualTo(houseUpdated)
    }
}