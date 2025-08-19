package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.models.TabBarUi

@Composable
fun SensorCardTabBar(
    onDomainClick: (String) -> Unit,
    uiData: TabBarUi,
) {
    ScrollableTabRow(
        selectedTabIndex = uiData.currentDomainPage,
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 16.dp
    ) {
        uiData.domains.forEachIndexed { index, domain ->
            Tab(
                selected = uiData.currentDomainPage == index,
                onClick = { onDomainClick(domain) },
                text = { Text(text = domain) }
            )
        }
    }
}