package app.trackly

import app.cash.turbine.test
import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryTest {

    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        repository = test1.FakeTaskRepository()
    }

    @Test
    fun insertTask_and_getAllTasks() = runTest {
        val task = Task(
            id = 1,
            sphereId = 2,
            content = "Test task",
            priority = 1
        )

        repository.insertTask(task)

        repository.getAllTasks().test {
            val tasks = awaitItem()
            assertEquals(1, tasks.size)
            assertEquals(task, tasks.first())
            cancel()
        }
    }

    @Test
    fun getTask_by_id() = runTest {
        val task = Task(
            id = 1,
            sphereId = 1,
            content = "Task",
            priority = 0
        )

        repository.insertTask(task)

        val result = repository.getTask(1)

        assertNotNull(result)
        assertEquals(task, result)
    }

    @Test
    fun deleteTask_removes_task() = runTest {
        val task = Task(
            id = 1,
            sphereId = 1,
            content = "Task",
            priority = 0
        )

        repository.insertTask(task)
        repository.deleteTask(task)

        val result = repository.getTask(1)
        assertNull(result)
    }

    @Test
    fun updateTask_updates_existing_task() = runTest {
        val task = Task(
            id = 1,
            sphereId = 1,
            content = "Old",
            priority = 0
        )

        repository.insertTask(task)

        val updated = task.copy(content = "New")
        repository.updateTask(updated)

        val result = repository.getTask(1)
        assertEquals("New", result?.content)
    }

    @Test
    fun getTasksBySphere_returns_only_matching_tasks() = runTest {
        val task1 = Task(
            id = 1,
            sphereId = 1,
            content = "A",
            priority = 0
        )

        val task2 = Task(
            id = 2,
            sphereId = 2,
            content = "B",
            priority = 1
        )

        repository.insertTask(task1)
        repository.insertTask(task2)

        repository.getTasksBySphere(1).test {
            val tasks = awaitItem()
            assertEquals(1, tasks.size)
            assertEquals(task1, tasks.first())
            cancel()
        }
    }
}
