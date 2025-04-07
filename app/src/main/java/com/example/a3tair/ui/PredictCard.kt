package com.example.a3tair.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3tair.R
import com.example.a3tair.models.Prediction
import com.example.a3tair.utils.Utils

@Composable
fun PredictionCard(prediction: Prediction) {
    Card (
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white_background)
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp, 12.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = Utils.formatDateTimeOnlyHour(prediction.createdAt),
                fontFamily = FontFamily(Font(R.font.be_vietnam_regular)),
                fontSize = 14.sp,
                color = colorResource(R.color.white)

            )

            Image(
                painterResource(R.drawable.dust),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
            )

            Card (
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFFFF)
                ),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    1.dp,
                    Color(Utils.colorList[prediction.airQuality])
                )

            ) {
                Text(
                    text = Utils.nameList[prediction.airQuality],
                    fontFamily = FontFamily(Font(R.font.be_vietnam_medium)),
                    fontSize = 12.sp,
                    color = Color(Utils.colorList[prediction.airQuality]),
                    modifier = Modifier
                        .padding(12.dp, 4.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Composable
fun RenderPredictCard(prediction: List<Prediction>) {
    prediction.forEach { it ->
        PredictionCard(it)
    }
}