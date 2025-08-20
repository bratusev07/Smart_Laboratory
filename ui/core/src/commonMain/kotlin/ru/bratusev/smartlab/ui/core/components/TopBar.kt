package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.bratusev.smartlab.ui.core.models.AppTopBarUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    uiData: AppTopBarUi,
    onTitleClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = Color.White,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.clickable { onTitleClick() },
                text = uiData.title,
            )
        },
        actions = {
            IconButton(
                onClick = onMenuClick,
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null
                )
            }
        }
    )
}