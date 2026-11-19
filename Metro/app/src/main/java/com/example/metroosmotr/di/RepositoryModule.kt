package com.example.metroosmotr.di

import com.example.metroosmotr.data.NetworkCheckerImpl
import com.example.metroosmotr.data.repository.DefectRepositoryImpl
import com.example.metroosmotr.domain.repository.DefectReader
import com.example.metroosmotr.domain.repository.DefectSyncSource
import com.example.metroosmotr.domain.repository.NetworkChecker
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
}