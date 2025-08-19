package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.TabBarUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardGridPagerUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorCardGridPager(
    modifier: Modifier = Modifier,
    uiData: SensorCardGridPagerUi,
) {
    val sensorsByDomain: Map<String, List<SensorCardUi>> by remember {
        derivedStateOf {
            uiData.sensors.groupBy { it.domain }
        }
    }
    val domains = remember(sensorsByDomain) { sensorsByDomain.keys.toList() }

    val pagerState: PagerState = rememberPagerState(pageCount = { sensorsByDomain.keys.size })
    val pagerScope: CoroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
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

        HorizontalPager(
            state = pagerState, modifier = modifier, pageSpacing = 40.dp
        ) { pageIndex ->
            val currentDomain = domains[pageIndex]
            val sensorsForPage = sensorsByDomain[currentDomain]!!

            SensorCardVerticalGrid(
                uiData = SensorCardVerticalGridUi(
                    sensors = sensorsForPage,
                    columnsAmount = 2
                )
            )
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun SensorCardGridPagerPreview() {
    val mockData = (
            buildList<SensorCardUi> {
                for (k in 1..5) {
                    for (i in 1..5) {
                        add(
                            SensorCardUi.Medium(
                                title = "Preview$i $k",
                                id = "Id$i",
                                state = SensorCardState.entries[(0..1).random()],
                                domain = "PreviewDomain$i",
                                drawableResource = SensorCardRes.thermometer,
                                tints = SensorCardTints.Common.Thermometer
                            )
                        )
                    }
                    for (i in 1..5) {
                        add(
                            SensorCardUi.Medium(
                                title = "Preview$i $k",
                                id = "Id$i",
                                state = SensorCardState.entries[(0..2).random()],
                                domain = "PreviewDomain$i",
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
                sensors = mockData,
                verticalGridsAtOneScreen = 1
            )
        )
    }
}