@file:OptIn(ExperimentalUuidApi::class)

package com.syouth.kmapper.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.syouth.kmapper.data.DependenciesContainer
import com.syouth.kmapper.domain.Result
import kmapper.examples.sampleapplication.composeapp.generated.resources.Res
import kmapper.examples.sampleapplication.composeapp.generated.resources.error_message
import kmapper.examples.sampleapplication.composeapp.generated.resources.error_unknown
import kmapper.examples.sampleapplication.composeapp.generated.resources.reload_message
import kmapper.examples.sampleapplication.composeapp.generated.resources.success_message
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi


@Composable
@Preview
internal fun App() {
    MaterialTheme {
        val viewModel: MainViewModel = viewModel(
            factory = MainViewModel.create(
                mainRepository = DependenciesContainer.mainRepository,
                mockDataHolder = DependenciesContainer.mockDataHolder,
                logger = DependenciesContainer.logger
            )
        )

        MainScreen(viewModel = viewModel)

    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.viewState.collectAsState()
    val isLoading = state.result is Result.Loading
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4)),
        contentAlignment = Alignment.Center
    ) {
        when (val result = state.result) {
            is Result.Loading -> LoadingView()
            is Result.Success<*> -> SuccessView(data = result.data)
            is Result.Error -> ErrorView(error = result.error)
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()

            ) {
                RefreshButton(onClick = viewModel::onRefresh)
            }
        }

    }
}

@Composable
private fun LoadingView() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_anim")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )
    Text(
        text = "🤔",
        fontSize = 64.sp,
        modifier = Modifier.offset(y = offsetY.dp)
    )
}

@Composable
private fun SuccessView(data: Any) {

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.5f),
        exit = fadeOut()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "😎", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.success_message),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = data.toString()
            )
        }
    }
}

@Composable
private fun ErrorView(error: Throwable) {
    var rotation by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "error_anim")
    rotation = infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake"
    ).value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "😢",
            fontSize = 64.sp,
            modifier = Modifier.rotate(rotation)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(
                Res.string.error_message,
                error.message ?: Res.string.error_unknown
            ),
            color = Color.Red
        )
    }
}

@Composable
private fun RefreshButton(onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = tween(100),
        label = "scale_btn"
    )
    TextButton(
        modifier = Modifier
            .scale(scale),
        onClick = {
            pressed = !pressed
            onClick()
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFF6200EE))
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = "🔄 " + stringResource(Res.string.reload_message),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

