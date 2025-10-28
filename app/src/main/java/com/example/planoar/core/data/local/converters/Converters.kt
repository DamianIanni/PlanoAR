package com.example.planoar.core.data.local.converters

import androidx.room.TypeConverter
import com.example.planoar.core.domain.model.DomainPoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.ProvidedTypeConverter
import javax.inject.Inject

/**
 * Le enseña a Room cómo convertir tipos complejos (como List<DomainPoint>)
 * a tipos simples (como String) para guardarlos en la base de datos.
 */
//@ProvidedTypeConverter
class Converters  {
    private val gson = Gson()

    @TypeConverter
    fun fromDomainPointList(points: List<DomainPoint>): String {
        // Convierte la lista de puntos a un string JSON
        return gson.toJson(points)
    }

    @TypeConverter
    fun toDomainPointList(pointsJson: String): List<DomainPoint> {
        // Necesitamos un 'TypeToken' para decirle a Gson que queremos
        // una List<DomainPoint> de vuelta, no un simple objeto.
        val listType = object : TypeToken<List<DomainPoint>>() {}.type
        return gson.fromJson(pointsJson, listType)
    }
}