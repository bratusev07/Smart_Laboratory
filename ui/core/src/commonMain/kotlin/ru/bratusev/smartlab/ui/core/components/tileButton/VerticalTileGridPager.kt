package ru.bratusev.smartlab.ui.core.components.tileButton

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.tileButton.TileButtonUi
import ru.bratusev.smartlab.ui.core.models.tileButton.VerticalTileGridUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun VerticalTileGridPager(
    modifier: Modifier = Modifier,
    data: List<VerticalTileGridUi>,
    pagerState: PagerState = rememberPagerState(pageCount = { data.size }),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val titles by remember {
        derivedStateOf {
            data.map { it.title }
        }
    }

    val titlesScrollState = rememberLazyListState()

    LaunchedEffect(pagerState.currentPage) {
        titlesScrollState.animateScrollToItem(pagerState.currentPage)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val halfScreenWidth = this.maxWidth / 2
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = titlesScrollState,
                contentPadding = PaddingValues(horizontal = halfScreenWidth - 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(count = titles.size, key = { it }) { titleIndex ->
                    Text(
                        modifier = Modifier.clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(titleIndex)
                            }
                        },
                        text = titles[titleIndex],
                        color = if (pagerState.currentPage == titleIndex) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
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
