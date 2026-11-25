package com.example.metroosmotr.di

import com.example.metroosmotr.data.FlashlightControllerImpl
import com.example.metroosmotr.data.LocationProviderImpl
import com.example.metroosmotr.data.NetworkCheckerImpl
import com.example.metroosmotr.data.repository.AuthRepositoryImpl
import com.example.metroosmotr.data.repository.DefectRepositoryImpl
import com.example.metroosmotr.data.repository.ReferenceRepositoryImpl
import com.example.metroosmotr.domain.repository.AuthRepository
import com.example.metroosmotr.domain.repository.DefectReader
import com.example.metroosmotr.domain.repository.DefectSyncSource
import com.example.metroosmotr.domain.repository.DefectWriter
import com.example.metroosmotr.domain.repository.FlashlightController
import com.example.metroosmotr.domain.repository.LocationProvider
import com.example.metroosmotr.domain.repository.NetworkChecker
import com.example.metroosmotr.domain.repository.ReferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindDefectReader(impl: DefectRepositoryImpl): DefectReader

    @Binds
    abstract fun bindDefectSyncSource(impl: DefectRepositoryImpl): DefectSyncSource

    @Binds
    abstract fun bindNetworkChecker(impl: NetworkCheckerImpl): NetworkChecker
    @Binds
    abstract fun bindDefectWriter(impl: DefectRepositoryImpl): DefectWriter

    @Binds
    abstract fun bindReferenceRepository(impl: ReferenceRepositoryImpl): ReferenceRepository

    @Binds
    abstract fun bindLocationProvider(impl: LocationProviderImpl): LocationProvider

    @Binds
    abstract fun bindFlashlight(impl: FlashlightControllerImpl): FlashlightController
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}