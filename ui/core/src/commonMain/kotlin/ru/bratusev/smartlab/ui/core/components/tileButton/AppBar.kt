package ru.bratusev.smartlab.ui.core.components.tileButton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.models.tileButton.AppBarUi

@Composable
fun AppBar(
    onTitleClick: (Int) -> Unit,
    uiData: AppBarUi,
) {
    ScrollableTabRow(
        selectedTabIndex = uiData.currentPageIndex,
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 16.dp
    ) {
        uiData.titles.forEachIndexed { index, title ->
            Tab(
                selected = uiData.currentPageIndex == index,
                onClick = { onTitleClick(index) },
                text = { Text(text = title) }
            )
        }
    }
}