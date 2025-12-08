package ru.bratusev.smartlab.ui.core.components.logcat

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Dangerous
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.LogcatMessageUi

@Composable
fun LogcatComponent(
    modifier: Modifier = Modifier,
    logcatMessageUi: LogcatMessageUi
) {
    var isExpanded by remember { mutableStateOf(false) }

    val style = getLogStyle(logcatMessageUi.type)

    Card(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, style.color.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            LogTypeIcon(style)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = logcatMessageUi.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    DateTimeBadge(logcatMessageUi.time, logcatMessageUi.date)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = logcatMessageUi.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 0.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun LogTypeIcon(style: LogStyle) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(style.color.copy(alpha = 0.15f))
            .border(1.dp, style.color.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = null,
            tint = style.color,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DateTimeBadge(time: String, date: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f), CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp
            )
        }
    }
}

data class LogStyle(
    val icon: ImageVector,
    val color: Color
)

@Composable
internal fun getLogStyle(type: String): LogStyle {
    return when (type.lowercase()) {
        "e", "error" -> LogStyle(
            icon = Icons.Rounded.Dangerous,
            color = Color(0xFFE53935) // Red
        )
        "w", "warning", "warn" -> LogStyle(
            icon = Icons.Rounded.WarningAmber,
            color = Color(0xFFFB8C00) // Orange
        )
        "d", "debug" -> LogStyle(
            icon = Icons.Rounded.BugReport,
            color = Color(0xFF43A047) // Green
        )
        "i", "info" -> LogStyle(
            icon = Icons.Rounded.Info,
            color = Color(0xFF1E88E5) // Blue
        )
        else -> LogStyle(
            icon = Icons.Rounded.Info,
            color = Color(0xFF757575) // Grey
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogcatComponent() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp).background(Color(0xFFFAFAFA)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LogcatComponent(
                modifier = Modifier.fillMaxWidth(),
                logcatMessageUi = LogcatMessageUi(
                    title = "Network Timeout",
                    description = "java.net.SocketTimeoutException: failed to connect to /192.168.1.55 (port 80) from /192.168.1.134 (port 44234) after 10000ms",
                    time = "14:23:45",
                    date = "15.12.2024",
                    type = "e"
                )
            )

            LogcatComponent(
                modifier = Modifier.fillMaxWidth(),
                logcatMessageUi = LogcatMessageUi(
                    title = "Deprecated API Usage",
                    description = "The method 'enableSomething()' is deprecated and will be removed in v2.0. Use 'enableSomethingNew()' instead.",
                    time = "14:20:12",
                    date = "15.12.2024",
                    type = "w"
                )
            )

            LogcatComponent(
                modifier = Modifier.fillMaxWidth(),
                logcatMessageUi = LogcatMessageUi(
                    title = "Payload Received",
                    description = "{ \"id\": 123, \"status\": \"active\", \"payload\": { \"temp\": 24.5, \"humidity\": 60 } }",
                    time = "14:18:33",
                    date = "15.12.2024",
                    type = "d"
                )
            )
        }
    }
}