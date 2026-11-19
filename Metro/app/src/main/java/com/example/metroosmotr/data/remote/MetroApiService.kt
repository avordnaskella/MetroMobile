package com.example.metroosmotr.data.remote

import com.example.metroosmotr.data.remote.dto.DefectDto
import com.example.metroosmotr.data.remote.dto.DefectPageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MetroApiService {
    @GET("defects")
    suspend fun getDefects(
        @Query("sectionId") sectionId: String? = null,
        @Query("limit") limit: Int = 20
    ): DefectPageDto

    @POST("defects")
    suspend fun createDefect(@Body defect: DefectDto): DefectDto
}