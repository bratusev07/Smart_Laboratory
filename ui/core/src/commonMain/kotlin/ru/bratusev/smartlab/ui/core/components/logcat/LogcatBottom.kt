package ru.bratusev.smartlab.ui.core.components.logcat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.april_short
import smartlaboratory.ui.core.generated.resources.august_short
import smartlaboratory.ui.core.generated.resources.december_short
import smartlaboratory.ui.core.generated.resources.february_short
import smartlaboratory.ui.core.generated.resources.january_short
import smartlaboratory.ui.core.generated.resources.july_short
import smartlaboratory.ui.core.generated.resources.june_short
import smartlaboratory.ui.core.generated.resources.march_short
import smartlaboratory.ui.core.generated.resources.may_short
import smartlaboratory.ui.core.generated.resources.november_short
import smartlaboratory.ui.core.generated.resources.october_short
import smartlaboratory.ui.core.generated.resources.september_short

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogcatDateBottomBar(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateRangeChanged: (LocalDate?, LocalDate?) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    val effectiveStart =
        startDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val effectiveEnd = endDate ?: effectiveStart

    fun addDays(amount: Int) {
        if (effectiveEnd != effectiveStart) {
            onDateRangeChanged(
                effectiveStart,
                effectiveEnd.plus(DatePeriod(days = amount)),
            )
        } else {
            onDateRangeChanged(
                effectiveStart.plus(DatePeriod(days = amount)),
                effectiveEnd.plus(DatePeriod(days = amount)),
            )
        }
    }

    if (showPicker) {
        val pickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = effectiveStart.atStartOfDayIn(TimeZone.UTC)
                .toEpochMilliseconds(),
            initialSelectedEndDateMillis = effectiveEnd.atStartOfDayIn(TimeZone.UTC)
                .toEpochMilliseconds()
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val s = pickerState.selectedStartDateMillis?.let {
                        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                    }
                    val e = pickerState.selectedEndDateMillis?.let {
                        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                    } ?: s

                    onDateRangeChanged(s, e)
                    showPicker = false
                }) { Text("Apply") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Cancel") } }
        ) {
            DateRangePicker(
                state = pickerState,
                title = { Text("Select Range", modifier = Modifier.padding(16.dp)) },
                showModeToggle = false
            )
        }
    }

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { addDays(-1) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Prev")
            }

            Card(
                onClick = { showPicker = true },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = formatDateRange(effectiveStart, effectiveEnd),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            IconButton(
                onClick = { addDays(1) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = "Next")
            }
        }
    }
}

@Composable
private fun formatDateRange(start: LocalDate, end: LocalDate): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    @Composable
    fun getMonthName(month: kotlinx.datetime.Month): String {
        val resource = when (month) {
            kotlinx.datetime.Month.JANUARY -> Res.string.january_short
            kotlinx.datetime.Month.FEBRUARY -> Res.string.february_short
            kotlinx.datetime.Month.MARCH -> Res.string.march_short
            kotlinx.datetime.Month.APRIL -> Res.string.april_short
            kotlinx.datetime.Month.MAY -> Res.string.may_short
            kotlinx.datetime.Month.JUNE -> Res.string.june_short
            kotlinx.datetime.Month.JULY -> Res.string.july_short
            kotlinx.datetime.Month.AUGUST -> Res.string.august_short
            kotlinx.datetime.Month.SEPTEMBER -> Res.string.september_short
            kotlinx.datetime.Month.OCTOBER -> Res.string.october_short
            kotlinx.datetime.Month.NOVEMBER -> Res.string.november_short
            else -> Res.string.december_short
        }
        return stringResource(resource)
    }

    @Composable
    fun format(d: LocalDate): String {
        return "${d.dayOfMonth} ${getMonthName(d.month)}"
    }

    return if (start == end) {
        format(start)
    } else "${format(start)} - ${format(end)}"
}

private fun LocalDate.atStartOfDayIn(timeZone: TimeZone): kotlinx.datetime.Instant {
    return kotlinx.datetime.LocalDateTime(this.year, this.month, this.dayOfMonth, 0, 0, 0, 0)
        .toInstant(timeZone)
}