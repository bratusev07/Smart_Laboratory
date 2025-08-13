package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.LogcatMessageUi

@Composable
fun LogcatComponent(modifier: Modifier, logcatMessageUi: LogcatMessageUi) {
    Column {
        Row (modifier) {
            Icon(Icons.Outlined.Home, contentDescription = null, modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically))
            LogcatInfo(Modifier.weight(1f).padding(horizontal = 4.dp).align(Alignment.CenterVertically), logcatMessageUi.title, logcatMessageUi.description)
            DateTimeComponent(Modifier.align(Alignment.CenterVertically), logcatMessageUi.time, logcatMessageUi.date)
        }

        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 7.dp, horizontal = 3.dp))
    }
}

@Composable
private fun LogcatInfo(modifier: Modifier, title: String, description: String) {
    Column(modifier) {
        Text(text = title)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = description)
    }
}

@Composable
private fun DateTimeComponent(modifier: Modifier, time: String, date: String) {
    Column(modifier = modifier) {
        Text(time)
        Text(date)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLog() {
    Column {
        LogcatComponent(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            logcatMessageUi = LogcatMessageUi(
                title = "Title",
                description = "SOme longSOme longSOme longSOme longSOme long description",
                time = "13:34:33",
                date = "11.11.2222",
                type = "random type"
            )
        )

        LogcatComponent(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            logcatMessageUi = LogcatMessageUi(
                title = "Title",
                description = "SOme longSOme longSOme longSOme longSOme long description",
                time = "13:34:33",
                date = "11.11.2222",
                type = "random type"
            )
        )

        LogcatComponent(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            logcatMessageUi = LogcatMessageUi(
                title = "Title",
                description = "SOme longSOme longSOme longSOme longSOme long description",
                time = "13:34:33",
                date = "11.11.2222",
                type = "random type"
            )
        )
    }
}