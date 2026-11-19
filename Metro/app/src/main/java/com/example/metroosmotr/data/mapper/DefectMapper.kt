package com.example.metroosmotr.data.mapper


import com.example.metroosmotr.data.local.entity.DefectEntity
import com.example.metroosmotr.data.remote.dto.DefectDto
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.DefectStatus
import com.example.metroosmotr.domain.model.Severity
import javax.inject.Inject

class DefectMapper @Inject constructor() {
    // DTO (Сервер) - Domain (Приложение)
    fun mapDtoToDomain(dto: DefectDto): Defect {
        return Defect(
            id = dto.id,
            sectionId = dto.sectionId,
            trackId = dto.trackId,
            categoryId = dto.categoryId,
            typeId = dto.typeId,
            picket = dto.picket,
            severity = try { Severity.valueOf(dto.severity) } catch (e: Exception) { Severity.LOW },
            status = try { DefectStatus.valueOf(dto.status) } catch (e: Exception) { DefectStatus.NEW },
            comment = dto.comment,
            detectedAt = dto.detectedAt,
            photoUrl = dto.photoUrl
        )
    }
    //  Domain (Приложение) - DTO (Сервер)
    fun mapDomainToDto(domain: Defect): DefectDto {
        return DefectDto(
            id = domain.id,
            sectionId = domain.sectionId,
            trackId = domain.trackId,
            categoryId = domain.categoryId,
            typeId = domain.typeId,
            picket = domain.picket,
            severity = domain.severity.name,
            status = domain.status.name,
            comment = domain.comment,
            detectedAt = domain.detectedAt,
            photoUrl = domain.photoUrl
        )
    }
    // 3. Entity (База данных Room) -> Domain (Приложение)  <-- ДОБАВИТЬ
    fun mapEntityToDomain(entity: DefectEntity): Defect {
        return Defect(
            id = entity.id,
            sectionId = entity.sectionId,
            trackId = entity.trackId,
            categoryId = entity.categoryId,
            typeId = entity.typeId,
            picket = entity.picket,
            severity = try { Severity.valueOf(entity.severity) } catch (e: Exception) { Severity.LOW },
            status = try { DefectStatus.valueOf(entity.status) } catch (e: Exception) { DefectStatus.NEW },
            comment = entity.comment,
            detectedAt = entity.detectedAt,
            photoUrl = entity.photoUrl
        )
    }

    // Domain (Приложение) - Entity (База данных Room)
    fun mapDomainToEntity(domain: Defect): DefectEntity {
        return DefectEntity(
            id = domain.id,
            sectionId = domain.sectionId,
            trackId = domain.trackId,
            categoryId = domain.categoryId,
            typeId = domain.typeId,
            picket = domain.picket,
            severity = domain.severity.name,
            status = domain.status.name,
            comment = domain.comment,
            detectedAt = domain.detectedAt,
            photoUrl = domain.photoUrl,
            isSynced = false // по умолчанию новая запись не отправлена на сервер
        )
    }
}