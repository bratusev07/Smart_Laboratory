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
import ru.bratusev.smartlab.ui.core.models.tileButton.AppBarUi
import ru.bratusev.smartlab.ui.core.models.tileButton.TileButtonUi
import ru.bratusev.smartlab.ui.core.models.tileButton.VerticalTileGridUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun VerticalTileGridPager(
    modifier: Modifier = Modifier,
    data: List<VerticalTileGridUi>,
    pagerState: PagerState = rememberPagerState(pageCount = { data.size }),
    pagerScope: CoroutineScope = rememberCoroutineScope(),
) {
    val titles by remember {
        derivedStateOf {
            data.map { it.title }
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
            VerticalTileGrid(
                uiData = data[pageIndex]
            )
        }
    }
}


@Preview(
    group = "VerticalTileGridPager", name = "", showBackground = true
)
@Composable
private fun VerticalTileGridPagerPreview() {
    val mockDataThermometers = VerticalTileGridUi(title = "Thermometers", buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.Thermometer(
                    location = "Preview$i",
                    temperature = (0..50).random().toFloat(),
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    })
    val mockDataLightBulbs = VerticalTileGridUi(title = "Light bulbs", buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.LightBulb(
                    location = "Preview$i",
                    isOn = (0..1).random() == 1,
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    })

    AppTheme {
        VerticalTileGridPager(
            data = listOf(
                mockDataLightBulbs, mockDataThermometers, mockDataLightBulbs, mockDataThermometers
            )
        )
    }
}