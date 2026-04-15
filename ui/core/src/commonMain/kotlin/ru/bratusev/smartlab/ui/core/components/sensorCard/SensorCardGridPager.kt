package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.models.TabBarUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardGridPagerUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorCardGridPager(
    modifier: Modifier = Modifier,
    uiData: SensorCardGridPagerUi,
    onSensorCardClicked: (String) -> Unit,
    onUpdateTimeOut: () -> Unit
) {
    val sensorsByDomain: Map<String, List<SensorCardUi.Tile>> by remember(uiData) {
        derivedStateOf {
            println("Derived state worked")
            uiData.sensors.groupBy { it.domain.name }
        }
    }
    val domains = remember(sensorsByDomain) { sensorsByDomain.keys.toList() }

    val pagerState: PagerState = rememberPagerState(pageCount = { sensorsByDomain.keys.size })
    val pagerScope: CoroutineScope = rememberCoroutineScope()

    LoadingIndicator(uiData.isUpdating, onTimeOut = onUpdateTimeOut)
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        SensorCardTabBar(
            onDomainClick = { domain ->
                pagerScope.launch {
                    val pageIndex = domains.indexOf(domain)
                    if (pageIndex != -1) {
                        pagerState.animateScrollToPage(pageIndex)
                    }
                }
            }, uiData = TabBarUi(
                domains = domains, currentDomainPage = pagerState.currentPage
            )
        )
        Box {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                pageSpacing = 40.dp
            ) { pageIndex ->
                val currentDomain = domains[pageIndex]
                val sensorsForPage = sensorsByDomain[currentDomain]!!

                SensorCardVerticalGrid(
                    uiData = SensorCardVerticalGridUi(
                        sensors = sensorsForPage, columnsAmount = 2
                    ), onSensorCardClicked = onSensorCardClicked
                )
            }
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun SensorCardGridPagerPreview() {
    val mockData = (buildList {
        for (k in 1..5) {
            for (i in 1..5) {
                add(
                    SensorCardUi.Tile.Medium(
                        title = "Preview$i $k",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.entries.random(),
                        drawableResource = SensorCardRes.thermometer,
                        tints = SensorCardTints.Common.Thermometer
                    )
                )
            }
            for (i in 1..5) {
                add(
                    SensorCardUi.Tile.Medium(
                        title = "Preview$i $k",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.entries.random(),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
    })

    AppTheme {
        SensorCardGridPager(
            uiData = SensorCardGridPagerUi(
                sensors = mockData, verticalGridsAtOneScreen = 1, isUpdating = false
            ),
            onSensorCardClicked = {}
        ) {}
    }
}