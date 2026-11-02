package app.trackly.domain.repository

import app.trackly.domain.model.Sphere
import kotlinx.coroutines.flow.Flow

interface SphereRepository {

    suspend fun insertSphere(sphere: Sphere)

    fun getAllSpheres(): Flow<List<Sphere>>

    suspend fun getSphere(id: Long?): Sphere?

    suspend fun updateSphere(sphere: Sphere)

    suspend fun deleteSphere(sphere: Sphere)
}