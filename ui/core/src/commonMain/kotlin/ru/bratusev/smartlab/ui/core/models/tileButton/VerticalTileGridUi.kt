package ru.bratusev.smartlab.ui.core.models.tileButton

data class VerticalTileGridUi(
    val title: String,
    val tiles: List<TileButtonUi>,
    val columnsAmount: Int = 2,
)