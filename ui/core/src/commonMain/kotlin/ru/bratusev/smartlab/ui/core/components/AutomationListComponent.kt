package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import ru.bratusev.smartlab.ui.core.models.AutomationUi

@Composable
fun AutomationListComponent(
    modifier: Modifier = Modifier.fillMaxSize(),
    automationUi: AutomationUi,
    onAutomationClicked: (String) -> Unit
) {
    LazyColumn(modifier) {
        items(automationUi.automationList.size) { index ->
            AutomationItemUiComponent(
                item = automationUi.automationList[index],
                onAutomationClicked = onAutomationClicked
            )
        }
    }
}

@Composable
private fun AutomationItemUiComponent(
    modifier: Modifier = Modifier,
    item: AutomationItemUi,
    onAutomationClicked: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = {
                onAutomationClicked(item.id)
            })
    ) {
        Column {
            Text(text = item.id, modifier = Modifier.fillMaxWidth())
            Text(text = item.description, modifier = Modifier.fillMaxWidth())
            Text(text = item.alias, modifier = Modifier.fillMaxWidth())
        }
    }
}