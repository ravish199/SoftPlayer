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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.reflect.InvocationTargetException

object Utils {



    @RequiresApi(Build.VERSION_CODES.Q)
    fun getImage(context: Context, uri: Uri): Bitmap {
        return  try{
            context.contentResolver.loadThumbnail(uri, Size(500, 500), null)
        }catch (e: Exception) {
            BitmapFactory.decodeResource(context.resources, R.drawable.music_symbols)
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatMillisToMinuteSecond(seconds: Long): String {
        val minutes = seconds / 60
        val seconds = seconds % 60
        return String.format(if(minutes < 10) AppConstant.TIME_FORMAT_1 else AppConstant.TIMEFORMAT_2, minutes, seconds)
    }

    val uiScope = CoroutineScope(Dispatchers.Main + Job())


}