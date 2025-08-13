package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.LogcatMessageUi

@Composable
fun LogcatComponent(modifier: Modifier = Modifier, logcatMessageUi: LogcatMessageUi) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Тип лога с иконкой и цветом
                LogTypeIndicator(logcatMessageUi.type)
                
                // Основная информация
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = logcatMessageUi.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = logcatMessageUi.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
                
                // Дата и время
                DateTimeComponent(
                    time = logcatMessageUi.time,
                    date = logcatMessageUi.date
                )
            }
        }
    }
}

@Composable
private fun LogTypeIndicator(type: String) {
    val icon: ImageVector
    val backgroundColor: Color
    val label: String
    val textColor: Color

    when (type.lowercase()) {
        // TODO Update icons
        "e", "error" -> {
            icon = Icons.Filled.Warning
            backgroundColor = Color(0xFFD32F2F)
            textColor = Color.White
            label = "ERROR"
        }
        "w", "warning" -> {
            icon = Icons.Filled.Warning
            backgroundColor = Color(0xFFFF9800)
            textColor = Color.White
            label = "WARN"
        }
        "d", "debug" -> {
            icon = Icons.Filled.Info
            backgroundColor = Color(0xFF2196F3)
            textColor = Color.White
            label = "DEBUG"
        }
        "i", "info" -> {
            icon = Icons.Filled.Info
            backgroundColor = Color(0xFF4CAF50)
            textColor = Color.White
            label = "INFO"
        }
        else -> {
            icon = Icons.Filled.Info
            backgroundColor = Color(0xFF9E9E9E)
            textColor = Color.White
            label = type.uppercase()
        }
    }
    
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DateTimeComponent(time: String, date: String) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogcatComponent() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Error log
        LogcatComponent(
            modifier = Modifier.fillMaxWidth(),
            logcatMessageUi = LogcatMessageUi(
                title = "Network Error",
                description = "Failed to connect to server. Connection timeout after 30 seconds. Please check your internet connection and try again.",
                time = "14:23:45",
                date = "15.12.2024",
                type = "e"
            )
        )
        
        // Warning log
        LogcatComponent(
            modifier = Modifier.fillMaxWidth(),
            logcatMessageUi = LogcatMessageUi(
                title = "Memory Warning",
                description = "Application memory usage is high (85%). Consider closing unused applications to free up memory.",
                time = "14:20:12",
                date = "15.12.2024",
                type = "w"
            )
        )
        
        // Debug log
        LogcatComponent(
            modifier = Modifier.fillMaxWidth(),
            logcatMessageUi = LogcatMessageUi(
                title = "API Request",
                description = "Sending GET request to /api/devices. Request ID: 12345, Headers: Content-Type: application/json",
                time = "14:18:33",
                date = "15.12.2024",
                type = "d"
            )
        )
        
        // Info log
        LogcatComponent(
            modifier = Modifier.fillMaxWidth(),
            logcatMessageUi = LogcatMessageUi(
                title = "User Login",
                description = "User successfully logged in. User ID: 12345, Session started at 14:15:00",
                time = "14:15:00",
                date = "15.12.2024",
                type = "i"
            )
        )
    }
}