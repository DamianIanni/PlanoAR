package com.example.planoar.core.domain.usecase.houseUseCases

import com.example.planoar.core.domain.model.House
import com.example.planoar.core.domain.repository.AppRepository
import javax.inject.Inject

class InsertHouseUseCase @Inject constructor(
    private val repository: AppRepository // Pide el "Menú" (Interfaz)
) {
    /**
     * Ejecuta el caso de uso.
     * @param name El nombre de la nueva casa.
     * @throws IllegalArgumentException Si el nombre está vacío o solo contiene espacios.
     */
    suspend operator fun invoke(name: String) {
        val trimmedName = name.trim() // Limpiamos espacios al principio y final

        // 1. Validación de entrada
        if (trimmedName.isBlank()) {
            throw IllegalArgumentException("El nombre de la casa no puede estar vacío.")
        }

        // 2. Regla de negocio: Comprobar duplicados
        if (repository.doesHouseExist(trimmedName)) {
            // Podríamos lanzar una excepción específica, devolver un Result,
            // o simplemente loggear y no hacer nada.
            // Por simplicidad, no hacemos nada si ya existe.
            println("WARN: La casa '$trimmedName' ya existe. No se insertará.")
            return // Salimos
        }

        // 3. Si todo está bien, creamos el objeto y llamamos al repositorio
        val newHouse = House(id = 0, name = trimmedName) // ID 0 para que Room lo genere
        repository.insertHouse(newHouse)
    }
}