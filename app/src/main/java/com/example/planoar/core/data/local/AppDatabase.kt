package com.example.planoar.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.planoar.core.data.local.converters.Converters // (Importa tu traductor)
import com.example.planoar.core.data.local.dao.HouseDao
import com.example.planoar.core.data.local.dao.RoomDao
import com.example.planoar.core.data.local.entities.HouseEntity
import com.example.planoar.core.data.local.entities.RoomEntity

@Database(
    entities = [
        HouseEntity::class, // (Le decimos que esta es una tabla)
        RoomEntity::class   // (Le decimos que esta es la otra tabla)
    ],
    version = 1, // (Empezamos en la versión 1)
    exportSchema = false // (No exportar el esquema por ahora)
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun houseDao(): HouseDao

    abstract fun roomDao(): RoomDao
}