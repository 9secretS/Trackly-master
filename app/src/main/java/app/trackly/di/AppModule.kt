package app.trackly.di

import app.trackly.data.repository.AuthRepositoryImpl
import app.trackly.data.repository.SphereRepositoryImpl
import app.trackly.data.repository.TaskRepositoryImpl
import app.trackly.domain.repository.AuthRepository
import app.trackly.domain.repository.SphereRepository
import app.trackly.domain.repository.TaskRepository
import app.trackly.domain.use_cases.sphere_use_cases.DeleteSphere
import app.trackly.domain.use_cases.sphere_use_cases.GetAllSpheres
import app.trackly.domain.use_cases.sphere_use_cases.GetSphere
import app.trackly.domain.use_cases.sphere_use_cases.InsertSphere
import app.trackly.domain.use_cases.sphere_use_cases.SphereUseCases
import app.trackly.domain.use_cases.sphere_use_cases.UpdateSphere
import app.trackly.domain.use_cases.task_use_cases.DeleteTask
import app.trackly.domain.use_cases.task_use_cases.GetAllTasks
import app.trackly.domain.use_cases.task_use_cases.GetTask
import app.trackly.domain.use_cases.task_use_cases.GetTasksBySphere
import app.trackly.domain.use_cases.task_use_cases.InsertTask
import app.trackly.domain.use_cases.task_use_cases.TaskUseCases
import app.trackly.domain.use_cases.task_use_cases.UpdateTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // AuthRepository
    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }

    // SphereRepository
    @Provides
    @Singleton
    fun provideSphereRepository(
        impl: SphereRepositoryImpl
    ): SphereRepository {
        return impl
    }

    //TaskRepository
    @Provides
    @Singleton
    fun provideTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository {
        return impl
    }

    //SphereUseCases
    @Provides
    @Singleton
    fun provideSphereUseCases(
        repository: SphereRepository
    ): SphereUseCases {
        return SphereUseCases(
            getAllSpheres = GetAllSpheres(repository),
            getSphere = GetSphere(repository),
            insertSphere = InsertSphere(repository),
            deleteSphere = DeleteSphere(repository),
            updateSphere = UpdateSphere(repository)
        )
    }

    //TaskUseCases
    @Provides
    @Singleton
    fun provideTaskUseCases(
        repository: TaskRepository
    ): TaskUseCases {
        return TaskUseCases(
            getAllTasks = GetAllTasks(repository),
            getTasksBySphere = GetTasksBySphere(repository),
            getTask = GetTask(repository),
            insertTask = InsertTask(repository),
            deleteTask = DeleteTask(repository),
            updateTask = UpdateTask(repository)
        )
    }
}