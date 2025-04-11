package com.example.a3tair.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
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
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontFamily
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.a3tair.R

class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            AirWidget()
        }
    }

}

@Composable
fun AirWidget(){
    val context = LocalContext.current
    val titleSize = 16.sp
    val valueSize = 60.sp
    Column (
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp, 24.dp, 12.dp, 0.dp)
            .background(Color(0x4DCECECE))
            .cornerRadius(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = "350.4",
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

//        Spacer(modifier = GlanceModifier.height(8.dp))

        Text(
            text = "Nguy hại",
            style = TextStyle(
                fontSize = titleSize,
                color = ColorProvider(
                    day = Color(0xFFFFFFFF),
                    night = Color(0xFFFFFFFF)
                )
            ),
            modifier = GlanceModifier.padding(0.dp, 12.dp)
        )

//        Spacer(modifier = GlanceModifier.height(8.dp))

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
                value = "34.4°C"
            )
            Spacer(
                modifier = GlanceModifier
                    .width(40.dp)
            )
            Card(
                icon = R.drawable.humidity,
                title = "Độ ẩm",
                value = "90%"
            )
        }
        Spacer(modifier = GlanceModifier.height(12.dp))

        Row(
            modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "Cập nhật lúc 11/04/2025 17:56",
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
                onClick = {
                    Toast.makeText(context, "Đang tải...", Toast.LENGTH_SHORT).show()
                },
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

