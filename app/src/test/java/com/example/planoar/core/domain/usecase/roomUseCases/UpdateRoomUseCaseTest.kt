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

class UpdateRoomUseCaseTest {

    private lateinit var fakeAppRepository: FakeAppRepository
    private lateinit var updateRoomUseCase: UpdateRoomUseCase

    @Before
    fun setup() {
        fakeAppRepository = FakeAppRepository()
        updateRoomUseCase = UpdateRoomUseCase(fakeAppRepository)
    }

    @After
    fun tearDown() {
        fakeAppRepository.reset()
    }

    @Test
    fun `Given a room when updateRoomUseCase is called then the room is updated`() = runTest {
        val house = House(id = 1,name = "Casa")
        val points = listOf(
            DomainPoint(x = 0f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 2f),
            DomainPoint(x = 0f, y = 0f, z = 2f)
        )
        val room = Room(1, 1,"Bathroom", points)
        val roomEdited =  Room(1, 1,"kitchen", points)
        fakeAppRepository.insertHouse(house)
        fakeAppRepository.insertRoom(room)
        assertThat(fakeAppRepository.lastInsertedRoom).isEqualTo(room)
        updateRoomUseCase(roomEdited)
        assertThat(fakeAppRepository.updateRoomCalledWith).isEqualTo(roomEdited)
    }
}