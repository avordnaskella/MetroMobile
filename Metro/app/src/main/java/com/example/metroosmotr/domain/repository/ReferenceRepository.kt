package com.example.metroosmotr.domain.repository

import com.example.metroosmotr.domain.model.DefectCategory
import com.example.metroosmotr.domain.model.DefectType
import com.example.metroosmotr.domain.model.Section

interface ReferenceRepository {
    suspend fun getSections(): List<Section>

    suspend fun getCategories(): List<DefectCategory>

    suspend fun getTypes(): List<DefectType>
}
