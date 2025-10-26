package com.example.planoar.core.domain.usecase

import com.example.planoar.core.domain.model.DomainPoint
import kotlin.math.abs

class CalculateAreaUseCase {
    operator fun invoke(points: List<DomainPoint>): Double {
        if (points.size < 3) {
            return 0.0
        }

        val points2D = points.map { it.x to it.z }

        var area = 0.0

        // Area = 0.5 * | (x1*z2 - z1*x2) + (x2*z3 - z2*x3) + ... + (xn*z1 - zn*x1) |
        for (i in points2D.indices) {
            val p1 = points2D[i]
            val p2 = points2D[(i + 1) % points2D.size] // El siguiente punto, o el primero si es el último

            area += (p1.first * p2.second - p1.second * p2.first)
        }

        return abs(area) / 2.0
    }

}