package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.bratusev.smartlab.ui.core.models.AppTopBarUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    uiData: AppTopBarUi,
    onTitleClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.clickable { onTitleClick() },
                text = uiData.title,
            )
        },
        actions = {
            content()
        }
    )
}