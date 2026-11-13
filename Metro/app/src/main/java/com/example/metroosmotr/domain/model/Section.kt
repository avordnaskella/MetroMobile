package com.example.metroosmotr.domain.model

data class Section(
    val id: String,
    val name: String,
    val start: Int,
    val end: Int,
    val tracks: List<Track>,
)

data class Track(
    val id: String,
    val name: String,
)
