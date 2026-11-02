package app.trackly.presentation.screens.sphere_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.domain.model.Task
import app.trackly.domain.model.TaskPriority
import app.trackly.domain.use_cases.task_use_cases.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SphereViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _sphereId = MutableStateFlow<Long?>(null)
    val sphereId = _sphereId.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    var tasksOrder = "default"

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksList: Flow<List<Task>> =
        sphereId
            .filterNotNull()
            .flatMapLatest { id ->
                taskUseCases.getTasksBySphere(id, order = tasksOrder)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun getSphereId(id: Long) {
        _sphereId.value = id
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                taskUseCases.deleteTask(task)
                _message.emit("Task deleted")
            } catch (e: ClientRequestException) {
                _message.emit("Failed to delete task")
            } catch (e: ServerResponseException) {
                _message.emit("Server error while deleting task")
            } catch (e: Exception) {
                _message.emit("Failed to delete task")
            }
        }
    }

    fun addTask(
        content: String,
        priority: TaskPriority
    ) {
        viewModelScope.launch {
            val currentSphereId = sphereId.value ?: return@launch

            try {
                taskUseCases.insertTask(
                    Task(
                        id = 0L,
                        sphereId = currentSphereId,
                        content = content,
                        priority = priority,
                        isCompleted = false
                    )
                )

                _message.emit("Task created")
            } catch (e: Exception) {
                _message.emit("Failed to create task")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskUseCases.updateTask(task)
            } catch (e: Exception) {
                _message.emit("Failed to update task")
            }
        }
    }
}