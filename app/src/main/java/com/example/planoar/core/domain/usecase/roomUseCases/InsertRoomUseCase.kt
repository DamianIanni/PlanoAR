package com.example.planoar.core.domain.usecase.roomUseCases

import com.example.planoar.core.domain.model.Room
import com.example.planoar.core.domain.repository.AppRepository
import jakarta.inject.Inject

class InsertRoomUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke (room: Room) {

        val trimmedName = room.name.trim()
        println("TRIMMED NAME $trimmedName")

        if (trimmedName.isBlank()) {
            throw IllegalArgumentException("El nombre del Room no puede estar vacío.")
        }

        if (repository.doesRoomExist(trimmedName, room.houseId)) {
            // Podríamos lanzar una excepción específica, devolver un Result,
            // o simplemente loggear y no hacer nada.
            // Por simplicidad, no hacemos nada si ya existe.
            println("WARN: La casa '$trimmedName' ya existe. No se insertará.")
            return // Salimos
        }

        repository.insertRoom(room)
    }
}