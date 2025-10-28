package com.example.planoar.core.data.di

import android.app.Application
import androidx.room.Room
import com.example.planoar.core.data.local.AppDatabase
import com.example.planoar.core.data.local.converters.Converters
import com.example.planoar.core.data.local.dao.HouseDao
import com.example.planoar.core.data.local.dao.RoomDao
import com.example.planoar.core.data.repository.AppRepositoryImpl
import com.example.planoar.core.domain.repository.AppRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "plano_ar_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppRepository(db: AppDatabase): AppRepository {
        return AppRepositoryImpl(db.houseDao(), db.roomDao(), provideGson())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

//    @Provides
//    @Singleton
//    fun provideConverters(gson: Gson): Converters { // <-- Esta función debe existir
//        return Converters(gson)
//    }
}