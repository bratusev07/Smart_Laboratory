package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onAutomationClicked(item.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.alias, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(item.description, color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Text("ID: ${item.id}", fontSize = 12.sp, color = Color.LightGray)
        }
    }
}