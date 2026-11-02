package app.trackly.data.repository

import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.toDomain
import app.trackly.data.remote.dto.toRequestDto
import app.trackly.domain.model.Sphere
import app.trackly.domain.repository.SphereRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SphereRepositoryImpl @Inject constructor(
    private val api: TracklyApi
) : SphereRepository {

    private val spheres = MutableStateFlow<List<Sphere>>(emptyList())

    override suspend fun insertSphere(sphere: Sphere) {
        val createdSphere = api.createSphere(
            sphere.toRequestDto()
        ).toDomain()

        spheres.value += createdSphere
    }

    override fun getAllSpheres(): Flow<List<Sphere>> = flow {
        refreshSpheres()
        emitAll(spheres)
    }


    override suspend fun getSphere(id: Long?): Sphere? {
        if (id == null) return null

        return api.getSphere(id).toDomain()
    }

    override suspend fun updateSphere(sphere: Sphere) {
        val updatedSphere = api.updateSphere(
            id = sphere.id,
            request = sphere.toRequestDto()
        ).toDomain()

        spheres.value = spheres.value.map {
            if (it.id == updatedSphere.id) updatedSphere else it
        }
    }

    override suspend fun deleteSphere(sphere: Sphere) {
        api.deleteSphere(sphere.id)

        spheres.value = spheres.value.filterNot {
            it.id == sphere.id
        }
    }

    private suspend fun refreshSpheres() {
        spheres.value = api.getSpheres().map {
            it.toDomain()
        }
    }
}