package ru.bratusev.smartlab.data.core.repository.preview

import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository

class ButtonTextRepositoryPreview : ButtonTextRepository {

    override suspend fun getText(): String {
        return "Preview Data level text"
    }

    override suspend fun setText(text: String) {

    }
}