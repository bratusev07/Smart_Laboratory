package ru.bratusev.smartlab.ui.core.components.tileButton

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
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardVerticalGrid
import ru.bratusev.smartlab.ui.core.models.AppBarUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun VerticalTileGridPager(
    modifier: Modifier = Modifier,
    uiData: List<SensorCardVerticalGridUi>,
    pagerState: PagerState = rememberPagerState(pageCount = { uiData.size }),
    pagerScope: CoroutineScope = rememberCoroutineScope(),
) {
    val titles by remember {
        derivedStateOf {
            uiData.map { it.title }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppBar(
            onTitleClick = { pageIndex ->
                pagerScope.launch {
                    pagerState.animateScrollToPage(pageIndex)
                }
            },
            uiData = AppBarUi(
                titles = titles,
                currentPageIndex = pagerState.currentPage
            )
        )

        HorizontalPager(
            state = pagerState,
            modifier = modifier,
            pageSpacing = 40.dp
        ) { pageIndex ->
            SensorCardVerticalGrid(
                uiData = uiData[pageIndex]
            )
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun SensorCardGridPagerPreview() {
    val mockDataThermometers = SensorCardVerticalGridUi(
        title = "Thermometers",
        buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Medium(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorCardState.entries[(0..1).random()],
                        domain = "PreviewDomain$i",
                        drawableResource = SensorCardRes.thermometer,
                        tints = SensorCardTints.Common.Thermometer
                    )
                )
            }
        },
        columnsAmount = 2
    )
    val mockDataLightBulbs = SensorCardVerticalGridUi(
        title = "Light bulbs",
        buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Medium(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorCardState.entries[(0..2).random()],
                        domain = "PreviewDomain$i",
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        },
        columnsAmount = 2
    )

    AppTheme {
        VerticalTileGridPager(
            uiData = listOf(
                mockDataLightBulbs, mockDataThermometers, mockDataLightBulbs, mockDataThermometers
            )
        )
    }
}