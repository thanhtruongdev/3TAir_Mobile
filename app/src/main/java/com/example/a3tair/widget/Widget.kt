package com.example.a3tair.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.a3tair.MainActivity
import com.example.a3tair.R
import com.example.a3tair.models.AirQuality
import com.example.a3tair.utils.Utils
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.TimeUnit
import androidx.glance.appwidget.GlanceAppWidgetManager

class Widget : GlanceAppWidget() {

    companion object {
        private const val TAG = "Widget"
        const val ACTION_UPDATE_WIDGET = "com.example.a3tair.ACTION_UPDATE_WIDGET"
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            AirWidget(context)
        }
    }

    suspend fun forceUpdate(context: Context) {
        Log.d(TAG, "Attempting to force update Widget")
        try {
            updateAll(context)
            Log.d(TAG, "Widget updateAll called")
            val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(Widget::class.java)
            glanceIds.forEach { glanceId ->
                update(context, glanceId)
                Log.d(TAG, "Updated Widget with glanceId: $glanceId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to updateAll: ${e.message}", e)
        }
    }
}

@Composable
fun AirWidget(context : Context){
    val titleSize = 16.sp
    val valueSize = 60.sp

    val sharedPrefs = context.getSharedPreferences("air_quality", Context.MODE_PRIVATE)

    val airQuality = AirQuality (
        sharedPrefs.getInt("aqi", 0),
        LocalDateTime.now().toString(),
        sharedPrefs.getFloat("pm25", 0f).toDouble(),
        sharedPrefs.getFloat("humidity", 0f).toDouble(),
        sharedPrefs.getInt("id", 0),
        sharedPrefs.getFloat("temperature", 0f).toDouble()
    )
    var now = Date()

    Column (
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp, 24.dp, 12.dp, 0.dp)
            .background(Color(0x4DCECECE))
            .cornerRadius(12.dp)
            .clickable(
                onClick = actionStartActivity<MainActivity>()
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Utils.locationName,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(
                    day = Color(0xFFFFFFFF),
                    night = Color(0xFFFFFFFF)
                )
            ),
            modifier = GlanceModifier.padding(0.dp, 12.dp)
        )
        Text(
            text = "PM2.5",
            style = TextStyle(
                fontSize = titleSize,
                color = ColorProvider(
                    day = Color(0xFFFFFFFF),
                    night = Color(0xFFFFFFFF)
                )
            )
        )
        Text(
            text = "${if(airQuality.dust == 0.0) 0 else String.format("%.2f", airQuality.dust)}",
            style = TextStyle(
                fontSize = valueSize,
                color = ColorProvider(
                    day = Color(0xFFFFFFFF),
                    night = Color(0xFFFFFFFF)
                )
            )
        )

        Text(
            text = "µg/m³",
            style = TextStyle(
                fontSize = titleSize,
                color = ColorProvider(
                    day = Color(0xFFFFFFFF),
                    night = Color(0xFFFFFFFF)
                )
            )
        )

        Text(
            text = "${Utils.nameList[airQuality.airQuality]}",
            style = TextStyle(
                fontSize = titleSize,
                color = ColorProvider(
                    day = Color(0xFFFFFFFF),
                    night = Color(0xFFFFFFFF)
                )
            ),
            modifier = GlanceModifier.padding(0.dp, 12.dp)
        )


        Row (
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally

        ) {
            Card(
                icon = R.drawable.sun,
                title = "Nhiệt độ",
                value = "${if (airQuality.temperature == 0.0) 0 else String.format("%.1f", airQuality.temperature)}°C"
            )
            Spacer(
                modifier = GlanceModifier
                    .width(40.dp)
            )
            Card(
                icon = R.drawable.humidity,
                title = "Độ ẩm",
                value = "${if (airQuality.humidity == 0.0) 0 else String.format("%.1f", airQuality.humidity) }%"
            )
        }
        Spacer(modifier = GlanceModifier.height(12.dp))

        Row(
            modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "Cập nhật lúc ${Utils.formatDateTimeFromSimple(now)}",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(
                        day = Color(0xFFFFFFFF),
                        night = Color(0xFFFFFFFF)
                    )
                ),
                modifier = GlanceModifier.defaultWeight()
            )
            Button(
                text = "Làm mới",
                onClick = actionRunCallback<ClickToUpdate>(),
                style = TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(
                        day = Color(0xFFFFFFFF),
                        night = Color(0xFFFFFFFF)
                    )
                )
            )
        }
    }
}

@Composable
fun Card(icon : Int, title : String, value : String) {
    val titleSize = 16.sp
    val valueSize = 28.sp
    val iconSize = 36.dp
    Row (
        modifier = GlanceModifier
            .wrapContentHeight(),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        
        Image(
            provider = ImageProvider(icon),
            contentDescription = null,
            modifier = GlanceModifier.size(iconSize)
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Column (
            modifier = GlanceModifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Text(
                text = "$title",
                style = TextStyle(
                    fontSize = titleSize,
                    color = ColorProvider(
                        day = Color(0xFFFFFFFF),
                        night = Color(0xFFFFFFFF)
                    )
                )
        
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = "$value",
                style = TextStyle(
                    fontSize = valueSize,
                    color = ColorProvider(
                        day = Color(0xFFFFFFFF),
                        night = Color(0xFFFFFFFF)
                    )
                )
            )
        }
    }
}

fun scheduleWidgetUpdate(context : Context) {
    val workRequest = PeriodicWorkRequestBuilder<WidgetWorker>(10, TimeUnit.MINUTES)
        .build()
    WorkManager.getInstance(context).enqueue(workRequest)
}

