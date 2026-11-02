@file:OptIn(ExperimentalMaterial3Api::class)

package app.trackly.presentation.screens.sphere_screen

import app.trackly.domain.model.TaskPriority
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.trackly.R
import app.trackly.domain.model.Task
import app.trackly.presentation.screens.home.SphereItem
import app.trackly.presentation.screens.sessionTimer.SessionTimerRoute
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.Border
import app.trackly.presentation.ui.theme.ButtonBg
import app.trackly.presentation.ui.theme.Gray
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.Orange
import app.trackly.presentation.ui.theme.PriorityBg
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.ShapeBg
import app.trackly.presentation.ui.theme.ShapeBorder
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow

@Composable
fun SphereScreen(
    id: Long,
    title: String,
    colorKey: String,
    navController: NavController,
    viewModel: SphereViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf("TIME") }
    val sessionsViewModel: SessionsViewModel = hiltViewModel()

    val tasks by viewModel.tasksList.collectAsState(emptyList())

    LaunchedEffect(id) { viewModel.getSphereId(id) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        containerColor = BackGr
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                // LABEL
                Text(
                    text = title,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 32.dp)
                        .fillMaxWidth()
                )

                // TOP CARD
                Box(
                    modifier = Modifier
                        .background(
                            color = ShapeBg,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = ShapeBorder,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(96.dp)
                ) {
                    Text(
                        text = if (selectedTab == "TASKS") {
                            "Tasks completed: "
                        } else {
                            "Time track"
                        },
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = Text,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                SphereTabs(
                    selectedTab = selectedTab,
                    onTabSelect = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedTab == "TIME") {
                    SessionsTabContent(
                        sphereId = id,
                        viewModel = sessionsViewModel,
                        onOpenTimer = { sphereId, sessionId, sessionTitle, planSeconds ->
                            navController.navigate(
                                SessionTimerRoute.Timer.createRoute(
                                    sphereId = sphereId,
                                    sessionId = sessionId,
                                    title = sessionTitle,
                                    planSeconds = planSeconds
                                )
                            )
                        }
                    )
                }

                if (selectedTab == "TASKS") {
                    // TASKS_TITLE
                    Row {
                        Text(
                            text = "Tasks:",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Text
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Checkbox(
                            onCheckedChange = {
                                viewModel.tasksOrder = "high_to_low"
                            },
                            checked = true,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Yellow,
                                checkmarkColor = ShapeBg,
                                uncheckedColor = Border
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DaysOfWeek()

                    Spacer(modifier = Modifier.height(16.dp))

                    // ADD_TASK_BUTTON
                    Box(
                        modifier = Modifier
                            .clickable(
                                onClick = { showDialog = true },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                            .drawBehind {
                                val strokeWidth = 2.dp.toPx()
                                val dashWidth = 8.dp.toPx()
                                val gapWidth = 8.dp.toPx()

                                drawRoundRect(
                                    color = ShapeBorder,
                                    style = Stroke(
                                        width = strokeWidth,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(dashWidth, gapWidth)
                                        )
                                    ),
                                    cornerRadius = CornerRadius(16.dp.toPx())
                                )
                            }
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(32.dp)
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.add_icon),
                                contentDescription = "add_icon",
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(32.dp)
                            )

                            Text(
                                text = "Add new task",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Medium,
                                color = Border,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if (showDialog) {
                        AddTaskDialog(
                            onDismiss = { showDialog = false },
                            onAdd = { content, priority ->
                                viewModel.addTask(content, priority)
                                showDialog = false
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            // TASKS
            if (selectedTab == "TASKS") {
                items(tasks, key = { it.id }) { task ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        positionalThreshold = { it * 0.75f },
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTask(task)
                            }

                            false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                        backgroundContent = { TaskDeleteBackground() }
                    ) {
                        TaskItem(
                            task,
                            onCheckedChange = { updated ->
                                viewModel.updateTask(updated)
                            }
                        )
                    }
                }
            }


        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Task) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp, end = 8.dp)
                .background(
                    color = when (task.priority) {
                        TaskPriority.HIGH -> Red
                        TaskPriority.MEDIUM -> Orange
                        TaskPriority.LOW -> BackGr
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .size(4.dp, 32.dp)
        )

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .background(
                    color = ShapeBg,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
                .height(64.dp)
                .padding(start = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.content,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = when(task.isCompleted) {
                        true -> TextDecoration.LineThrough
                        else -> TextDecoration.None
                    },
                    color = when(task.isCompleted) {
                        true -> Gray
                        else -> Text
                    },
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Checkbox(
                    onCheckedChange = {
                        onCheckedChange(
                            task.copy(isCompleted = !task.isCompleted)
                        )
                    },
                    checked = task.isCompleted,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Yellow,
                        checkmarkColor = ShapeBg,
                        uncheckedColor = Border
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun DaysOfWeek() {
    val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {},
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShapeBg,
                    contentColor = GrayText
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp)
                    .weight(1f)
            ) {
                Text(
                    text = it,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }

}

@Composable
fun TaskDeleteBackground() {
    Box(
        modifier = Modifier
            .padding(bottom = 16.dp, start = 12.dp)
            .fillMaxSize()
            .background(
                color = Red,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            painter = painterResource(R.drawable.delete),
            contentDescription = "delete",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String, TaskPriority) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.LOW) }
    val context = LocalContext.current

    AlertDialog(
        containerColor = ShapeBg,
        titleContentColor = Text,
        onDismissRequest = { onDismiss() },
        title = { Text(
            text = "Add new task",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = Text,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        ) },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = content,
                    onValueChange = { content = it },
                    label = {
                        Text(
                            text = "Enter your task",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Normal,
                        ) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Yellow,
                        focusedLabelColor = Yellow,
                        unfocusedLabelColor = GrayText,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Choose priority",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Text,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    PriorityItem(
                        text = "Low",
                        color = Gray,
                        selected = selectedPriority == TaskPriority.LOW,
                        onClick = { selectedPriority = TaskPriority.LOW }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    PriorityItem(
                        text = "Mid",
                        color = Orange,
                        selected = selectedPriority == TaskPriority.MEDIUM,
                        onClick = { selectedPriority = TaskPriority.MEDIUM }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    PriorityItem(
                        text = "High",
                        color = Red,
                        selected = selectedPriority == TaskPriority.HIGH,
                        onClick = { selectedPriority = TaskPriority.HIGH }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        content.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Enter the sphere name",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        else -> {
                            onAdd(content, selectedPriority)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBg,
                    contentColor = Text
                )
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBg,
                    contentColor = Text
                )
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
fun PriorityItem(
    text: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (selected) PriorityBg else ShapeBg,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            )
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .size(3.dp, 16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = Text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium
        )
    }
}




@Preview
@Composable
fun Prev() {
    Row() {
        Text(
            text = "Tasks:",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Text
        )

        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            onCheckedChange = {

            },
            checked = true,
            colors = CheckboxDefaults.colors(
                checkedColor = Yellow,
                checkmarkColor = ShapeBg,
                uncheckedColor = Border
            ),
            modifier = Modifier
                .padding(end = 8.dp)
        )
    }
}

@Composable
private fun SphereTabs(
    selectedTab: String,
    onTabSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onTabSelect("TIME") },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTab == "TIME") ShapeBg else BackGr,
                contentColor = if (selectedTab == "TIME") Text else GrayText
            )
        ) {
            Text(
                text = "Time track",
                fontFamily = Montserrat
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { onTabSelect("TASKS") },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTab == "TASKS") ShapeBg else BackGr,
                contentColor = if (selectedTab == "TASKS") Text else GrayText
            )
        ) {
            Text(
                text = "Tasks list",
                fontFamily = Montserrat
            )
        }
    }
}
