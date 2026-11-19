package com.example.metroosmotr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "defects")
data class DefectEntity(
    @PrimaryKey val id: String,
    val sectionId: String,
    val trackId: String,
    val categoryId: Int,
    val typeId: Int,
    val picket: Int,
    val severity: String,
    val status: String,
    val comment: String?,
    val detectedAt: String,
    val photoUrl: String?,
    val isSynced: Boolean = false // флаг: отправлено на сервер или нет
)