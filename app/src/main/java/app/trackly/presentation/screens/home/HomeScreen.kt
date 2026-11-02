package app.trackly.presentation.screens.home

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.trackly.R
import app.trackly.domain.model.Sphere
import app.trackly.presentation.screens.add_sphere.AddSphereRoute
import app.trackly.presentation.screens.sphereColorByKey
import app.trackly.presentation.screens.sphereIconByKey
import app.trackly.presentation.screens.sphere_screen.SphereScreenRoute
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.Blue
import app.trackly.presentation.ui.theme.Border
import app.trackly.presentation.ui.theme.ButtonBg
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Green
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.Orange
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.ShapeBg
import app.trackly.presentation.ui.theme.ShapeBorder
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val spheres by viewModel.spheresList.collectAsState(emptyList())

    val uiState by viewModel.uiState.collectAsState()

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

    val interactionSource = remember { MutableInteractionSource() }


    Scaffold(
        containerColor = BackGr
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                // LABEL
                Text(
                    text = stringResource(id = R.string.home_title),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 32.dp)
                        .fillMaxWidth()
                )

                // STRICK
                WeeklyReviewCard(
                    analytics = uiState.weeklyAnalytics,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // SPHERES_TITLE
                Text(
                    text = stringResource(R.string.spheres),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Text
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ADD_SPHERE_BUTTON
                Box(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navController.navigate(AddSphereRoute.AddSphere.route)
                            },
                            indication = null,
                            interactionSource = interactionSource
                        ) // showing dialogue
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
                    Row(modifier = Modifier
                        .align(Alignment.Center),
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
                            text = stringResource(R.string.add_sphere),
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            color = Border,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // SPHERES
            items(spheres, key = { it.id }) { sphere ->
                val dismissState = rememberSwipeToDismissBoxState(
                    positionalThreshold = { it * 0.75f },
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart) viewModel.deleteSphere(sphere)
                        false
                    }
                )

                val itemInteractionSource = remember { MutableInteractionSource() }

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    enableDismissFromEndToStart = true,
                    backgroundContent = { DeleteBackground() },
                    modifier = Modifier.clickable(
                        interactionSource = itemInteractionSource,
                        indication = null,
                        onClick = {
                            navController.navigate(
                                route = SphereScreenRoute.Sphere.createRoute(
                                    id = sphere.id,
                                    title = sphere.title,
                                    colorKey = sphere.colorKey
                                )
                            )
                        }
                    )
                ) {
                    SphereItem(
                        sphere
                    )
                }
            }
        }
    }
}


@Composable
fun SphereItem(
    sphere: Sphere,
) {
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
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = sphereColorByKey(sphere.colorKey),
                        shape = CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(id = sphereIconByKey(sphere.iconKey)),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier
                .fillMaxHeight()
                .width(16.dp))

            Text(
                text = sphere.title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Text,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.arrow),
                contentDescription = "Arrow",
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}

@Composable
fun AddSphereDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("RED") }
    val context = LocalContext.current

    AlertDialog(
        containerColor = ShapeBg,
        titleContentColor = Text,
        onDismissRequest = onDismiss,
        title = { Text(
            text = stringResource(id = R.string.add_sphere),
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
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_name),
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
                    text = stringResource(id = R.string.choose_the_color),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Text,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedColor == "RED",
                        onClick = { selectedColor = "RED" },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Red,
                            selectedColor = Red
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = selectedColor == "ORANGE",
                        onClick = { selectedColor = "ORANGE" },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Orange,
                            selectedColor = Orange
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = selectedColor == "YELLOW",
                        onClick = { selectedColor = "YELLOW" },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Yellow,
                            selectedColor = Yellow
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = selectedColor == "GREEN",
                        onClick = { selectedColor = "GREEN" },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Green,
                            selectedColor = Green
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = selectedColor == "BLUE",
                        onClick = { selectedColor = "BLUE" },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Blue,
                            selectedColor = Blue
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        title.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Enter the sphere name",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        else -> {
                            onAdd(title, selectedColor)
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
                onClick = onDismiss,
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
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
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

@Preview(showSystemUi = true)
@Composable
fun Prev() {
}
