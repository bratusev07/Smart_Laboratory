package ru.bratusev.smartlab.feature_areas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import ru.bratusev.smartlab.ui.core.theme.AppTheme

// TODO: Сделать уведомления, что сохранение произошло успешно.
// Также нужно всегда убеждаться, что сохранение произошло и тогда разблокировать навигацию.
// Иначе есть риск тупо не сохранить ничего.
// Похожий механизм уже есть на экране добавления виджетов.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreasScreen(
    vm: AreasScreenViewModel = koinViewModel(),
    setMenuAction: (action: () -> Unit) -> Unit,
    goToAddWidgetScreen: () -> Unit,
) {
    val state = vm.uiState.collectAsState()

}

@Composable
private fun MenuDropDown(
    isExpanded: Boolean,
    onAddScreen: () -> Unit,
    onEditMode: () -> Unit,
    onClose: () -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded, onDismissRequest = onClose, content = {
            DropdownMenuItem(
                text = { Text("Добавить виджет") },
                onClick = onAddScreen,
                leadingIcon = {
                    Icon(
                        Icons.Default.Add, contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text("Редактировать") },
                onClick = onEditMode,
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit, contentDescription = null
                    )
                })
        })
}

@Preview(
    showBackground = true
)
@Composable
private fun CustomScreenPreview() {
    startKoin {
        modules(areasScreenModulePreview)
    }
    AppTheme {
        AreasScreen(
            setMenuAction = {},
            goToAddWidgetScreen = {}
        )
    }
}