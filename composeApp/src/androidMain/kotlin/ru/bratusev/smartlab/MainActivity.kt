package ru.bratusev.smartlab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplicationPreview
import org.koin.dsl.module
import ru.bratusev.smartlab.di.appModulePreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}


@Composable
@Preview
private fun AppPreview() {
    // В других местах эта херня вроде не особо нужна
    // TODO: быть может можно как-то contextModule сразу впихнуть в appModulePreview.
    // Потом получится так, что не нужно будет такое городить.
    val context = LocalContext.current
    val contextModule = module {
        single<Context> { context }
    }
    KoinApplicationPreview(application = {
        modules(appModulePreview, contextModule)
    }) {
        App()
    }
}