package ru.bratusev.smartlab.feature_logcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.bratusev.smartlab.domain.core.usecase.GetLogcatMessagesUseCase
import ru.bratusev.smartlab.feature_logcat.mappers.mapToUi
import ru.bratusev.smartlab.feature_logcat.models.Event
import ru.bratusev.smartlab.feature_logcat.models.LogcatState

class LogcatViewModel(
    private val getLogcatMessagesUseCase: GetLogcatMessagesUseCase
) : ViewModel() {

    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private val _uiState = MutableStateFlow(
        LogcatState(
            startDate = today,
            endDate = today,
            isLoading = true
        )
    )
    val uiState: StateFlow<LogcatState> = _uiState.asStateFlow()

    init {
        init()
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> { /* Navigation */ }

            is Event.OnDateRangeChanged -> {
                _uiState.update {
                    it.copy(startDate = event.startDate, endDate = event.endDate)
                }
            }

            is Event.OnTypeToggled -> {
                _uiState.update { state ->
                    val currentTypes = state.selectedTypes.toMutableSet()
                    if (currentTypes.contains(event.type)) currentTypes.remove(event.type)
                    else currentTypes.add(event.type)

                    state.copy(selectedTypes = currentTypes, isLoading = true)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun init() {
        val typesFlow = _uiState
            .map { it.selectedTypes }
            .distinctUntilChanged()

        val repositoryFlow = typesFlow
            .flatMapLatest { types ->
                getLogcatMessagesUseCase(logTypes = types.toList())
            }

        val dateFilterFlow = _uiState
            .map { it.startDate to it.endDate }
            .distinctUntilChanged()

        combine(
            repositoryFlow,
            dateFilterFlow
        ) { result, (startDate, endDate) ->

            val allMessages = result.getOrElse { emptyList() }

            if (startDate == null && endDate == null) {
                allMessages
            } else {
                val startIso = startDate?.toString()
                val endIso = endDate?.toString()

                allMessages.filter { msg ->
                    val dbDate = msg.date ?: ""
                    val msgIsoDate = convertDbDateToIso(dbDate)

                    val afterStart = startIso?.let { msgIsoDate >= it } ?: true
                    val beforeEnd = endIso?.let { msgIsoDate <= it } ?: true
                    afterStart && beforeEnd
                }
            }
        }.onEach { filteredMessages ->
            _uiState.update {
                it.copy(
                    messages = filteredMessages.map { msg -> msg.mapToUi() },
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    // Converts "dd.MM.yyyy" (DB format) to "yyyy-MM-dd" (ISO format)
    private fun convertDbDateToIso(dbDate: String): String {
        val parts = dbDate.split(".")
        return if (parts.size == 3) {
            val day = parts[0]
            val month = parts[1]
            val year = parts[2]
            "$year-$month-$day"
        } else {
            dbDate
        }
    }
}