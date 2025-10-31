package com.example.planoar.core.domain.usecase.houseUseCases

import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.repository.AppRepository
import jakarta.inject.Inject

class UpdateHouseUseCase @Inject constructor(
    private val repository: AppRepository
){
    suspend operator fun invoke(house: House) {
        repository.updateHouse(house)
    }
}