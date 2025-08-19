package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.components.widgets.SensorsWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi

@Composable
fun CustomWidget(uiData: CustomWidgetUi) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        when (uiData) {
            is CustomWidgetUi.SensorsList -> SensorsWidget(
                uiData = uiData,
                onToggle = {},
                header = {
                    Row(
                        modifier = Modifier.background(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ).padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Виджет с id: ${uiData.id}",
                        )
                        WidgetToolBar(
                            onEditClick = {},
                            onAddClick = {}
                        )
                    }
                }
            )
        }
    }
}