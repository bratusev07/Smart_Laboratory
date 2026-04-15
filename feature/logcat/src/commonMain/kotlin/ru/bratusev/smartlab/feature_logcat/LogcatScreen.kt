package ru.bratusev.smartlab.feature_logcat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_logcat.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.logcat.LogcatComponent
import ru.bratusev.smartlab.ui.core.components.logcat.LogcatDateBottomBar
import ru.bratusev.smartlab.ui.core.components.logcat.LogcatHeader
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.nothing_found

@Composable
fun LogcatScreen(
    vm: LogcatViewModel = koinViewModel(),
    navigationApi: NavigationApi,
) {
    val state by vm.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LogcatHeader(
                selectedTypes = state.selectedTypes,
                onTypeToggled = { vm.handleEvent(Event.OnTypeToggled(it)) }
            )
        },
        bottomBar = {
            LogcatDateBottomBar(
                startDate = state.startDate,
                endDate = state.endDate,
                onDateRangeChanged = { start, end ->
                    vm.handleEvent(Event.OnDateRangeChanged(start, end))
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (state.messages.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding() + 8.dp
                    )
                ) {
                    items(
                        items = state.messages,
                    ) { message ->
                        LogcatComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            logcatMessageUi = message
                        )
                    }
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (!state.isLoading && state.messages.isEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(Res.string.nothing_found),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}