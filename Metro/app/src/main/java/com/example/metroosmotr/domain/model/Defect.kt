package com.example.metroosmotr.domain.model

import java.util.UUID

enum class Severity { LOW, MEDIUM, CRITICAL }

enum class DefectStatus { NEW, IN_PROGRESS, FIXED, VERIFIED }

data class Defect(
    val id: String = UUID.randomUUID().toString(), // Генерируем на клиенте (по ТЗ)
    val sectionId: String,
    val trackId: String,
    val categoryId: Int,
    val typeId: Int,
    val picket: Int,
    val severity: Severity,
    val status: DefectStatus = DefectStatus.NEW,
    val comment: String? = null,
    val detectedAt: String, // В ТЗ формат ISO 8601 (String)
    val photoUrl: String? = null,
)
