package app.trackly.domain.use_cases.sphere_use_cases

data class SphereUseCases(
    val getAllSpheres: GetAllSpheres,
    val getSphere: GetSphere,
    val insertSphere: InsertSphere,
    val deleteSphere: DeleteSphere,
    val updateSphere: UpdateSphere
)