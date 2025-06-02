package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.repository.ButtonTextRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository

val dataModule = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryImpl()
    }
}