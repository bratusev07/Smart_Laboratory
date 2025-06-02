package ru.bratusev.smartlab.data.core.repository

import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository

class ButtonTextRepositoryImpl : ButtonTextRepository {

    override suspend fun getText(): String {
        return "Data level text"
    }

    override suspend fun setText(text: String) {

    }
}