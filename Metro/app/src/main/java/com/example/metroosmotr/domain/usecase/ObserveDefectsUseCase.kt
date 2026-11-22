package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.repository.DefectReader
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class ObserveDefectsUseCase @Inject constructor(
    private val reader: DefectReader,
) {
    operator fun invoke(): Flow<List<Defect>> = reader.observeDefects()
}
