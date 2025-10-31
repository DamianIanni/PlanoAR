package com.example.planoar.core.domain.usecase.roomUseCases

import com.example.planoar.core.domain.model.Room
import com.example.planoar.core.domain.repository.AppRepository
import jakarta.inject.Inject

class DeleteRoomUseCase @Inject constructor (
    private val repository: AppRepository
) {
    suspend operator fun invoke(room: Room) {
        repository.deleteRoom(room)

    }
}