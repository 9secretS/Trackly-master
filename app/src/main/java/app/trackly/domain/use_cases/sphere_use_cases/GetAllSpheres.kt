package app.trackly.domain.use_cases.sphere_use_cases

import app.trackly.domain.model.Sphere
import app.trackly.domain.repository.SphereRepository
import kotlinx.coroutines.flow.Flow

class GetAllSpheres(
    private val repository: SphereRepository
) {
    operator fun invoke(): Flow<List<Sphere>> {
        return repository.getAllSpheres()
    }
}