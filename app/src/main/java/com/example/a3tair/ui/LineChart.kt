package com.example.a3tair.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3tair.R
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun LineChart(
    data: List<Double>,
    label: String,
    lineColor : List<Color>,
    firstGradientFillColor : Color,
    minValue : Double,
    maxValue : Double,
    dotColor : Color
) {
    LineChart(
        data = remember {
            listOf(
                Line(
                    label = label,
                    values = data,
                    color = Brush.radialGradient(
                        colors = lineColor
                    ),
                    firstGradientFillColor = firstGradientFillColor.copy(alpha = 0.8f),
                    secondGradientFillColor = Color.Transparent,
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(dotColor),
                        strokeWidth = 1.dp,
                        animationSpec = tween(800)
                    ),
                    popupProperties = PopupProperties(
                        containerColor = firstGradientFillColor,
                        textStyle = TextStyle.Default.copy(
                            color = Color.White
                        ),
                        contentVerticalPadding = 4.dp,
                        contentHorizontalPadding = 6.dp
                    )
                )
            )
        },
        minValue = minValue,
        maxValue = maxValue,
        modifier = Modifier.height(300.dp).padding(12.dp, 0.dp, 12.dp, 12.dp),
        dividerProperties = DividerProperties(
            yAxisProperties = LineProperties(
                color = SolidColor(Color.White)
            ),
            xAxisProperties = LineProperties(
                color = SolidColor(Color.White)
            )
        ),
        gridProperties = GridProperties(
            enabled = false
        ),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = TextStyle.Default.copy(
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = TextStyle.Default.copy(
                color = Color.White,
                fontSize = 12.sp
            ),
            padding = 4.dp
        )
    )
}