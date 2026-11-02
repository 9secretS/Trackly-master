package app.trackly.domain.use_cases.sphere_use_cases

import app.trackly.domain.model.Sphere
import app.trackly.domain.repository.SphereRepository

class DeleteSphere(
    private val repository: SphereRepository
) {
    suspend operator fun invoke(sphere: Sphere) {
        repository.deleteSphere(sphere)
    }
}