package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.loading

@Composable
fun LoadingIndicator(
    show: Boolean,
    text: String = stringResource(Res.string.loading),
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(show, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 36.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(text)
        }
    }
}