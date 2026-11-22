package com.example.metroosmotr.data.repository

import com.example.metroosmotr.domain.model.DefectCategory
import com.example.metroosmotr.domain.model.DefectType
import com.example.metroosmotr.domain.model.Section
import com.example.metroosmotr.domain.model.Track
import com.example.metroosmotr.domain.repository.ReferenceRepository
import javax.inject.Inject

class ReferenceRepositoryImpl @Inject constructor() : ReferenceRepository {

    // Пока возвращаем захардкоженные данные (как на макете),
    // чтобы валидация пикета работала.
    override suspend fun getSections(): List<Section> {
        return listOf(
            Section(
                id = "a1b2c3d4-0000-4000-8000-000000000001",
                name = "Перегон А — Б",
                start = 10, // Начало участка
                end = 42,   // Конец участка
                tracks = listOf(Track("a1b2c3d4-0000-4000-8000-0000000000a1", "Главный путь 1"))
            )
        )
    }

    override suspend fun getCategories(): List<DefectCategory> {
        return listOf(DefectCategory(1, "Рельсовое полотно"))
    }

    override suspend fun getTypes(): List<DefectType> {
        return listOf(DefectType(1, "Трещина"))
    }
}