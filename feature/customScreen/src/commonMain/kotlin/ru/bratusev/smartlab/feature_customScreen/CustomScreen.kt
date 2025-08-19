package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

@Composable
fun CustomScreen(
    vm: CustomScreenViewModel = koinViewModel(),
    navigationApi: NavigationApi,
    setMenuAction: (action: () -> Unit) -> Unit,
) {
    val state = vm.uiState.collectAsState()

    // TODO: может что-то лучше можно придумать
    DisposableEffect(Unit) {
        setMenuAction {
            vm.handleEvent(Event.OnMenuButtonClicked)
        }

        onDispose {
            setMenuAction { }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        var data by remember {
            mutableStateOf(buildList {
                for (i in 1..30) {
                    add(
                        SensorCardUi.Widget.Row(
                            title = "Preview$i",
                            id = "Id$i",
                            state = SensorCardState.entries[(0..2).random()],
                            domain = "PreviewDomain$i",
                            drawableResource = SensorCardRes.lightBulb,
                            tints = SensorCardTints.Common.LightBulb
                        )
                    )
                }
            })
        }
        CustomWidget(
            uiData = CustomWidgetUi.SensorsList(
                sensors = data, id = 1
            )
        )
        Text("Is open ${state.value.isModalOpen}")
    }
}