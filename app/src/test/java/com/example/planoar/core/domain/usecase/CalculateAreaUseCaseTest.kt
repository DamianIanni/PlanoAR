package com.example.planoar.core.domain.usecase

import com.example.planoar.core.domain.model.DomainPoint
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CalculateAreaUseCaseTest {
    @Test
    fun `dado un poligono simple (cuadrado 2x2), calcula el area correcta`() {
        val useCase = CalculateAreaUseCase()

        val points = listOf(
            DomainPoint(x = 0f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 0f),
            DomainPoint(x = 2f, y = 0f, z = 2f),
            DomainPoint(x = 0f, y = 0f, z = 2f)
        )

        val expectedArea = 4.0 // 2 * 2 = 4

        val calculatedArea = useCase(points)

        assertThat(calculatedArea).isWithin(0.001).of(expectedArea)
    }
}