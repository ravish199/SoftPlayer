package com.ravish.softplayer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.data.model.ImageSize
import com.ravish.softplayer.data.model.ShadowType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object Utils {


    @RequiresApi(Build.VERSION_CODES.Q)
    fun getImage(context: Context, uri: Uri): Bitmap {
        return getBitmap(context, uri, ImageSize.LARGE)
    }

    fun getImageSmall(context: Context, uri: Uri?): Bitmap {
        return getBitmap(context, uri, ImageSize.SMALL)
    }


    private fun getBitmap(context: Context, uri: Uri?, imageSize: ImageSize): Bitmap {
        return try {
            context.contentResolver.loadThumbnail(
                uri!!,
                Size(imageSize.width, imageSize.height),
                null
            )
        } catch (e: Exception) {
            BitmapFactory.decodeResource(
                context.resources, if (imageSize == ImageSize.SMALL)
                    R.drawable.music_symbols_small else R.drawable.music_symbols_large
            )
        }
    }

    fun getImage(context: Context, image: Bitmap?): ImageBitmap {
        return (image ?: BitmapFactory.decodeResource(
            context.resources,
            R.drawable.app_background
        )).asImageBitmap()
    }

    @SuppressLint("ModifierFactoryUnreferencedReceiver")
    fun Modifier.drawShadow(shadowType: ShadowType, shadowWidth: Dp): Modifier {
        return this.drawWithContent {
            drawContent()
            val startY = if (shadowType == ShadowType.TOP) {
                shadowWidth.toPx()
            } else {
                size.height - shadowWidth.toPx()
            }

            val endY = if (shadowType == ShadowType.TOP) {
                0.dp.toPx()
            } else {
                size.height
            }
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
                    startY = startY,
                    endY = endY
                )
            )
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatMillisToMinuteSecond(seconds: Long): String {
        val minutes = seconds / 60
        val seconds = seconds % 60
        return String.format(
            if (minutes < 10) AppConstant.TIME_FORMAT_1 else AppConstant.TIMEFORMAT_2,
            minutes,
            seconds
        )
    }

    val uiScope = CoroutineScope(Dispatchers.Main + Job())


}