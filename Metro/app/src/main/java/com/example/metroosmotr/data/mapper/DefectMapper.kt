package com.example.metroosmotr.data.mapper

import com.example.metroosmotr.data.remote.dto.DefectDto
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.DefectStatus
import com.example.metroosmotr.domain.model.Severity
import javax.inject.Inject

class DefectMapper @Inject constructor() {
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
}