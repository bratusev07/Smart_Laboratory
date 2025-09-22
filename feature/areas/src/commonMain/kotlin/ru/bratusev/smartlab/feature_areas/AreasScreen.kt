package ru.bratusev.smartlab.feature_areas

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import ru.bratusev.smartlab.ui.core.components.AreaCard
import ru.bratusev.smartlab.ui.core.theme.AppTheme

// TODO: Сделать уведомления, что сохранение произошло успешно.
// Также нужно всегда убеждаться, что сохранение произошло и тогда разблокировать навигацию.
// Иначе есть риск тупо не сохранить ничего.
// Похожий механизм уже есть на экране добавления виджетов.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreasScreen(
    vm: AreasScreenViewModel = koinViewModel(),
) {
    val state = vm.uiState.collectAsState()
    LazyColumn {
        items(state.value.areas, key = { it.areaId }) {
            AreaCard(uiData = it)
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AreasScreenPreview() {
    startKoin {
        modules(areasScreenModulePreview)
    }
    AppTheme {
        AreasScreen(
        )
    }
}