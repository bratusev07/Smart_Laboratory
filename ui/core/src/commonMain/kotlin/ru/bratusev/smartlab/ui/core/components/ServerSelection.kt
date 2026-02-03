package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.ServerSelectionUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun ServerSelection(
    modifier: Modifier = Modifier, uiData: ServerSelectionUi, onSelect: (url: String) -> Unit
) {
    LazyColumn(
        modifier = modifier.background(
            MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.large
        ), contentPadding = PaddingValues(8.dp)
    ) {
        items(uiData.serverList.keys.toList()) { url ->
            if (uiData.serverList.isNotEmpty()) {
                ServerItem(
                    url = url,
                    name = uiData.serverList.getOrElse(url) { "Ошибка" },
                    selected = uiData.currentServerUrl == url,
                    onClick = {
                        onSelect(url)
                    })
            } else {
                Text("Список серверов пуст")
            }
        }
        item {
            ServerItem(url = "Добавить", name = "+", selected = false, onClick = {})
        }
    }
}

@Composable
private fun ServerItem(
    modifier: Modifier = Modifier, url: String, name: String, selected: Boolean, onClick: () -> Unit
) {
    Row(
        modifier = modifier.height(36.dp).fillMaxWidth().background(
                if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            ).clickable {
                onClick()
            }, Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Text(">")
        }
        Text(
            modifier = Modifier.weight(1f).padding(start = 8.dp),
            text = url,
            color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.ExtraBold else null
        )
        Text(
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            text = name,
            color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.ExtraBold else null
        )
        if (selected) {
            Text("<")
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun PreviewServerSelection() {
    AppTheme {
        ServerSelection(
            uiData = ServerSelectionUi(
                serverList = mapOf("255.255.255.255" to "Preview", "254.254.254.254" to "Preview2"),
                currentServerUrl = "254.254.254.254"
            ), onSelect = {})
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun PreviewServerItem() {
    AppTheme {
        ServerItem(
            url = "255.255.255.255", name = "Preview", selected = false, onClick = {})
    }
}
