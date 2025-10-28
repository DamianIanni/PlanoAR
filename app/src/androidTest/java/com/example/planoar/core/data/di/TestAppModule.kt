package com.example.planoar.core.data.di

import android.content.Context
import androidx.room.Room
import com.example.planoar.core.data.local.AppDatabase
import com.example.planoar.core.data.repository.AppRepositoryImpl
import com.example.planoar.core.domain.repository.AppRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideTestAppRepository(db: AppDatabase, gson: Gson): AppRepository {
        return AppRepositoryImpl(db.houseDao(), db.roomDao(), gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}