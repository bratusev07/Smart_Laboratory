package ru.bratusev.smartlab.domain.core.repository

interface ButtonTextRepository {

    suspend fun getText() : String

    suspend fun setText(text: String)
}