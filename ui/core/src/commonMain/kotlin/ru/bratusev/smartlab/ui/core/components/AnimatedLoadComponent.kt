package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.models.AnimatedLoadUi
import ru.bratusev.smartlab.ui.core.resources.StringsRes

@Composable
fun AnimatedLoadComponent(modifier: Modifier, animatedLoadUi: AnimatedLoadUi) {
    AnimatedVisibility(
        modifier = modifier,
        visible = animatedLoadUi.isVisible
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                maxLines = 1,
                text = "${StringsRes.CURRENT_STAGE}: ${animatedLoadUi.stageName}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alpha(0.85f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.6f)
            )
        }
    }
}