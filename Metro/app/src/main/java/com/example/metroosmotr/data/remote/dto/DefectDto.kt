package com.example.metroosmotr.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DefectDto(
    val id: String,
    val sectionId: String,
    val trackId: String,
    val categoryId: Int,
    val typeId: Int,
    val picket: Int,
    val severity: String,
    val status: String,
    val comment: String? = null,
    val detectedAt: String,
    val photoUrl: String? = null
)

@Serializable
data class DefectPageDto(
    val items: List<DefectDto>,
    val nextCursor: String? = null,
    val hasMore: Boolean
)