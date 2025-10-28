package com.example.planoar.core.data.di

import com.example.planoar.core.data.repository.AppRepositoryImpl
import com.example.planoar.core.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Este es el "manual" que le dice a Hilt (el "Mozo"):
 * "Cuando alguien pida el Menú (AppRepository)...
 * ... entrégale la Cocina (AppRepositoryImpl)"
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository
}