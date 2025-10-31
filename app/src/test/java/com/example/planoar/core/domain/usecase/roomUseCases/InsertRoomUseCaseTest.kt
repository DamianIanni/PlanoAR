package com.example.planoar.core.domain.usecase.roomUseCases

import com.example.planoar.FakeAppRepository
import com.example.planoar.core.domain.model.DomainPoint
import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.model.Room
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class InsertRoomUseCaseTest {
    private lateinit var fakeAppRepository: FakeAppRepository
    private lateinit var insertRoomUseCase: InsertRoomUseCase

    @Before
    fun setup() {
        fakeAppRepository = FakeAppRepository()
        insertRoomUseCase = InsertRoomUseCase(fakeAppRepository)

    }

    @After
    fun tearDown() {
        fakeAppRepository.reset()
    }

    @Test
    fun `given a new room with a valid name, it should be inserted successfully`() = runTest {
        val house = House(1, "Casita")
        val points = listOf(
            DomainPoint(x = 0f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 2f),
            DomainPoint(x = 0f, y = 0f, z = 2f)
        )
        val room = Room(1, 1,"Habitacion", points)

        fakeAppRepository.insertHouse(house)

        insertRoomUseCase(room)

        assertThat(fakeAppRepository.getRoomsForHouse(house.id).first().contains(room))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a new room with an invalid name, it should not be inserted`() = runTest {
        val house = House(1, "Casita")
        val points = listOf(
            DomainPoint(x = 0f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 2f),
            DomainPoint(x = 0f, y = 0f, z = 2f)
        )
        val room = Room(1, 1,"   ", points)

        fakeAppRepository.insertHouse(house)

        try {
            insertRoomUseCase(room)
        } finally {
            assertThat(fakeAppRepository.doesRoomExistCalledWith).isNull()
            assertThat(fakeAppRepository.insertRoomCalled).isFalse()
        }
    }
}