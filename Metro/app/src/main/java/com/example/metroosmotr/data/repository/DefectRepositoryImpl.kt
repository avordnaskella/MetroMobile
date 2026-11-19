package com.example.metroosmotr.data.repository

import com.example.metroosmotr.data.mapper.DefectMapper
import com.example.metroosmotr.data.remote.MetroApiService
import com.example.metroosmotr.data.remote.NetworkErrorMapper
import com.example.metroosmotr.domain.model.AppError
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.repository.DefectReader
import com.example.metroosmotr.domain.repository.DefectSyncSource
import com.example.metroosmotr.domain.repository.NetworkChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefectRepositoryImpl @Inject constructor(
    private val api: MetroApiService,
    private val mapper: DefectMapper,
    private val networkChecker: NetworkChecker
) : DefectReader, DefectSyncSource {

    override suspend fun fetchFromServer(): List<Defect> {
        if (!networkChecker.isNetworkAvailable()) throw AppError.Network()
        return try {
            api.getDefects().items.map { mapper.mapDtoToDomain(it) }
        } catch (e: Exception) {
            throw NetworkErrorMapper.map(e)
        }
    }

    override suspend fun pushToServer(defect: Defect): Defect {
        if (!networkChecker.isNetworkAvailable()) throw AppError.Network()
        return try {
            val dto = mapper.mapDomainToDto(defect)
            mapper.mapDtoToDomain(api.createDefect(dto))
        } catch (e: Exception) {
            throw NetworkErrorMapper.map(e)
        }
    }

    override fun observeDefects(): Flow<List<Defect>> = flow {
        emit(fetchFromServer())
    }

    override suspend fun getDefectById(id: String): Defect? = null
    override suspend fun getUnsyncedDefects(): List<Defect> = emptyList()
    override suspend fun markAsSynced(id: String) {}
}