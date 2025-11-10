package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.loading

@Composable
fun LoadingIndicator(
    show: Boolean,
    text: String = stringResource(Res.string.loading),
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier.zIndex(2f),
        visible = show, enter = fadeIn(animationSpec = tween(100)) + scaleIn(
            animationSpec = tween(300), initialScale = 0.8f
        ),
        exit = fadeOut(animationSpec = tween(100)) + scaleOut(
            animationSpec = tween(300), targetScale = 0.8f
        )
    ) {
        Column {

            Box(
                Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .zIndex(3f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.Center)
                        .padding(top = 36.dp)
                        .dropShadow(MaterialTheme.shapes.medium) {
                            radius = 5.dp.toPx()
                            spread = 0.5.dp.toPx()
                            color = Color.Gray
                        }
                        .background(
                            MaterialTheme.colorScheme.surfaceContainer,
                            shape = MaterialTheme.shapes.medium
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
                        text = text
                    )
                    LinearProgressIndicator(
                        modifier = Modifier.padding(
                            bottom = 12.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                    )
                }
            }
            Spacer(
                Modifier.fillMaxHeight().zIndex(3f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    ),
            )
        }

    }
}

@Preview(
    showBackground = true,
    heightDp = 300
)
@Composable
private fun LoadingIndicatorPreview() {
    AppTheme {
        LoadingIndicator(show = true)
    }
}