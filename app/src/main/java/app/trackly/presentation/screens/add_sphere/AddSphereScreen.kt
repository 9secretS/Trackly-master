package app.trackly.presentation.screens.add_sphere

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.trackly.R
import app.trackly.presentation.screens.auth.PrimaryAuthButton
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.Blue
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Green
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.Orange
import app.trackly.presentation.ui.theme.PrimeWhite
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow

private data class SphereColorOption(
    val key: String,
    val color: androidx.compose.ui.graphics.Color
)

private data class SphereIconOption(
    val key: String,
    @DrawableRes val iconRes: Int
)

private val sphereColors = listOf(
    SphereColorOption("RED", Red),
    SphereColorOption("ORANGE", Orange),
    SphereColorOption("YELLOW", Yellow),
    SphereColorOption("GREEN", Green),
    SphereColorOption("BLUE", Blue)
)

private val sphereIcons = listOf(
    SphereIconOption("FINANCE", R.drawable.finance_icon),
    SphereIconOption("GROWTH", R.drawable.growth_icon),
    SphereIconOption("STUDY", R.drawable.study_icon),
    SphereIconOption("HEALTH", R.drawable.health_icon),
    SphereIconOption("PAINT", R.drawable.piaint_icon),
    SphereIconOption("COMMUNITY", R.drawable.community_icon),
    SphereIconOption("HOME", R.drawable.home_icon),
    SphereIconOption("CARDIO", R.drawable.cardio_icon),
    SphereIconOption("SPORT", R.drawable.sport_icon),
    SphereIconOption("LAPTOP", R.drawable.laptop_icon),
    SphereIconOption("WRITE", R.drawable.write_icon),
    SphereIconOption("WORK", R.drawable.work_icon)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddSphereScreen(
    onClose: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AddSphereViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect {
            onSaved()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGr)
            .padding(horizontal = 28.dp)
            .padding(top = 44.dp, bottom = 36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cancel),
                contentDescription = "Close",
                modifier = Modifier
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClose()
                    }
            )

            Text(
                text = "Add new area",
                color = Text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(42.dp))

        Text(
            text = "Enter the name:",
            color = Text,
            fontFamily = Montserrat,
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(14.dp))

        SphereNameField(
            value = state.title,
            onValueChange = viewModel::onTitleChange,
            isError = state.titleError
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Choose the color:",
            color = Text,
            fontFamily = Montserrat,
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            sphereColors.forEach { option ->
                ColorOptionItem(
                    color = option.color,
                    selected = state.selectedColorKey == option.key,
                    onClick = {
                        viewModel.onColorSelect(option.key)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(42.dp))

        Text(
            text = "Choose the icon:",
            color = Text,
            fontFamily = Montserrat,
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(22.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            sphereIcons.forEach { option ->
                IconOptionItem(
                    iconRes = option.iconRes,
                    selected = state.selectedIconKey == option.key,
                    onClick = {
                        viewModel.onIconSelect(option.key)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryAuthButton(
            text = if (state.isLoading) "Saving..." else "Save",
            enabled = !state.isLoading && state.title.isNotBlank(),
            onClick = viewModel::saveSphere
        )
    }
}

@Composable
private fun SphereNameField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val borderColor = if (isError) {
        Red
    } else {
        PrimeWhite.copy(alpha = 0.35f)
    }

    val backgroundColor = if (isError) {
        Red.copy(alpha = 0.10f)
    } else {
        PrimeWhite.copy(alpha = 0.08f)
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            color = Text,
            fontFamily = Montserrat,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(Yellow),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (value.isBlank()) {
                    Text(
                        text = "Name",
                        color = GrayText,
                        fontFamily = Montserrat,
                        fontSize = 22.sp
                    )
                }

                innerTextField()

                Text(
                    text = "${value.length}/50",
                    color = GrayText,
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 2.dp)
                )
            }
        }
    )
}

@Composable
private fun ColorOptionItem(
    color: androidx.compose.ui.graphics.Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .border(
                width = if (selected) 4.dp else 1.dp,
                color = if (selected) {
                    color.copy(alpha = 0.95f)
                } else {
                    PrimeWhite.copy(alpha = 0.18f)
                },
                shape = CircleShape
            )
            .padding(6.dp)
            .background(
                color = color,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    )
}

@Composable
private fun IconOptionItem(
    @DrawableRes iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderAlpha = if (selected) 0.65f else 0.22f
    val backgroundAlpha = if (selected) 0.24f else 0.08f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(62.dp)
            .border(
                width = 1.dp,
                color = PrimeWhite.copy(alpha = borderAlpha),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = PrimeWhite.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(34.dp)
        )
    }
}



