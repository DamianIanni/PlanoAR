package com.example.planoar.core.domain.usecase.houseUseCases

import com.example.planoar.FakeAppRepository
import com.example.planoar.core.domain.model.House
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeleteHouseUseCaseTest {
    private lateinit var fakeAppRepository: FakeAppRepository
    private lateinit var deleteHouseUseCase: DeleteHouseUseCase

    @Before
    fun setup () {
        fakeAppRepository = FakeAppRepository()
        deleteHouseUseCase = DeleteHouseUseCase(fakeAppRepository)
    }

    @After
    fun tearDown() {
        fakeAppRepository.reset()
    }

    @Test
    fun `Given a house, when deleteHouse is called, then the house is deleted`() = runTest {
        val house = House(id = 1, name = "Lichi house")
        fakeAppRepository.insertHouse(house)
        val getHouses = fakeAppRepository.getAllHouses()
        assertThat(house).isIn(getHouses.first())
        deleteHouseUseCase(house)
        assertThat(getHouses.first()).isEmpty()
    }
}