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

class DeleteRoomUseCaseTest {
    private var fakeAppRepository = FakeAppRepository()
    private var deleteRoomUseCase = DeleteRoomUseCase(fakeAppRepository)

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {
        fakeAppRepository.reset()
    }

    @Test
    fun `Given a room, it should be deleted`() = runTest {
        val house = House(id = 1,name = "Casa")
        val points = listOf(
            DomainPoint(x = 0f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 2f),
            DomainPoint(x = 0f, y = 0f, z = 2f)
        )
        val room = Room(1, 1,"   ", points)
        fakeAppRepository.insertHouse(house)
        fakeAppRepository.insertRoom(room)
        assertThat(fakeAppRepository.lastInsertedRoom).isEqualTo(room)
        deleteRoomUseCase(room)
        assertThat(fakeAppRepository.lastInsertedRoom).isNotIn(fakeAppRepository.getRoomsForHouse(1).first())
    }
}