package ru.bratusev.smartlab.ui.core.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed class AppIcon {
    data class Vector(val imageVector: ImageVector) : AppIcon()
    data class Resource(val resource: DrawableResource) : AppIcon()
    data class Url(val url: String) : AppIcon()
}

@Composable
fun AppIcon.asPainter(): Painter {
    return when (this) {
        is AppIcon.Vector -> rememberVectorPainter(image = this.imageVector)
        is AppIcon.Resource -> painterResource(resource = this.resource)
        is AppIcon.Url -> {

            val context = LocalPlatformContext.current

            val request = ImageRequest.Builder(context)
                .data(this.url)
                .size(256, 256)
                .build()

            rememberAsyncImagePainter(model = request)
        }
    }
}