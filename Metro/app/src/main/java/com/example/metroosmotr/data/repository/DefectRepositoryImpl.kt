package com.example.metroosmotr.data.repository

import com.example.metroosmotr.data.local.dao.DefectDao
import com.example.metroosmotr.data.mapper.DefectMapper
import com.example.metroosmotr.data.remote.MetroApiService
import com.example.metroosmotr.data.remote.NetworkErrorMapper
import com.example.metroosmotr.domain.model.AppError
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.DefectStatus
import com.example.metroosmotr.domain.repository.DefectReader
import com.example.metroosmotr.domain.repository.DefectSyncSource
import com.example.metroosmotr.domain.repository.DefectWriter
import com.example.metroosmotr.domain.repository.NetworkChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefectRepositoryImpl @Inject constructor(
    private val api: MetroApiService,
    private val mapper: DefectMapper,
    private val networkChecker: NetworkChecker,
    private val dao: DefectDao
) : DefectReader, DefectSyncSource, DefectWriter { // <-- ДОБАВИЛИ DefectWriter

    // --- DefectReader (Чтение) ---
    override fun observeDefects(): Flow<List<Defect>> {
        return dao.observeAll().map { entities ->
            entities.map { mapper.mapEntityToDomain(it) }
        }
    }

    override suspend fun getDefectById(id: String): Defect? {
        return dao.getById(id)?.let { mapper.mapEntityToDomain(it) }
    }

    // --- DefectWriter (Запись) ---
    override suspend fun saveDefect(defect: Defect) {
        // Сохраняем локально (Offline-First). На сервер уйдет при синхронизации.
        dao.insert(mapper.mapDomainToEntity(defect))
    }

    override suspend fun updateStatus(id: String, newStatus: DefectStatus) {
        // TODO: Реализовать обновление статуса в DAO
    }

    override suspend fun attachPhoto(id: String, photoPath: String) {
        // TODO: Реализовать прикрепление фото в DAO
    }

    // --- DefectSyncSource (Синхронизация) ---
    override suspend fun fetchFromServer(): List<Defect> {
        if (!networkChecker.isNetworkAvailable()) throw AppError.Network()
        return try {
            val remoteDefects = api.getDefects().items.map { mapper.mapDtoToDomain(it) }
            dao.insertAll(remoteDefects.map { mapper.mapDomainToEntity(it).copy(isSynced = true) })
            remoteDefects
        } catch (e: Exception) {
            throw NetworkErrorMapper.map(e)
        }
    }

    override suspend fun pushToServer(defect: Defect): Defect {
        if (!networkChecker.isNetworkAvailable()) throw AppError.Network()
        return try {
            val dto = mapper.mapDomainToDto(defect)
            val responseDto = api.createDefect(dto)
            dao.markAsSynced(defect.id)
            mapper.mapDtoToDomain(responseDto)
        } catch (e: Exception) {
            throw NetworkErrorMapper.map(e)
        }
    }

    override suspend fun getUnsyncedDefects(): List<Defect> {
        return dao.getUnsynced().map { mapper.mapEntityToDomain(it) }
    }

    override suspend fun markAsSynced(id: String) {
        dao.markAsSynced(id)
    }
}