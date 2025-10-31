package com.example.planoar.core.domain.usecase.houseUseCases

import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.repository.AppRepository
import jakarta.inject.Inject

class DeleteHouseUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(house: House) {
        appRepository.deleteHouse(house)
    }
}