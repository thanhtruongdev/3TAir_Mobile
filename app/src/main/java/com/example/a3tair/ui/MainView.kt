package com.example.a3tair.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.a3tair.R
import com.example.a3tair.models.AirQuality
import com.example.a3tair.utils.Utils
import com.example.a3tair.viewModel.AiViewModel
import com.example.a3tair.viewModel.AirQualityViewModel
import java.time.LocalDateTime
import java.util.Date

@Composable
fun MainView(viewModel : AirQualityViewModel, aiViewModel : AiViewModel) {

    val context = LocalContext.current

    var updatedTime by remember {
        mutableStateOf(Date())
    }

    var locationCity = viewModel.locationState.collectAsState().value
    var airQuality = AirQuality(0, LocalDateTime.now().toString(), 0.0, 0.0, 0, 0.0)
    val airQualityList = viewModel.airQuality.observeAsState().value?.toList()
    val aiAdvice = aiViewModel.advice.collectAsState().value ?: ""

    LaunchedEffect(Unit) {
        viewModel.fetchLocation(context)
        aiViewModel.generateAdvice(Utils.nameList[airQuality.airQuality])
    }

    if (airQualityList != null && airQualityList.isNotEmpty()) {
        airQuality = airQualityList?.get(0) ?: AirQuality(0, LocalDateTime.now().toString(), 0.0, 0.0, 0, 0.0)
    }

    val prediction = viewModel.prediction.observeAsState().value?.toList()

    Box (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painterResource(R.drawable.background_1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )

        ConstraintLayout (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            val (locationIcon, locationLabel, locationName,
                txtUpdateTime, reloadIcon) = createRefs()

            val (tempCard, humidCard, dustCard, airQualityCard) = createRefs()

            val bottomCardBarrier = createBottomBarrier(tempCard, humidCard)

            val (txtPredictFuture, cardRow) = createRefs()

            val (airQualityHistoryChart, tempHistoryChart, humidHistoryChart) = createRefs()

            Image(
                painterResource(R.drawable.baseline_location_on_24),
                contentDescription = "Location Icon",
                modifier = Modifier
                    .constrainAs(locationIcon) {
                        top.linkTo(parent.top, margin = 40.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                    }
                    .size(60.dp)
            )

            Text(
                text = "Địa điểm",
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                modifier = Modifier
                    .constrainAs (locationLabel) {
                        top.linkTo(locationIcon.top)
                        start.linkTo(locationIcon.end, margin = 12.dp)
                    },
                fontSize = 14.sp
            )

            Text(
                text = locationCity.toString(),
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.be_vietnam_semibold)),
                modifier = Modifier
                    .constrainAs (locationName) {
                        top.linkTo(locationLabel.bottom, margin = 4.dp)
                        start.linkTo(locationIcon.end, margin = 12.dp)
                    },
                fontSize = 28.sp
            )

            Text(
                text = "Cập nhật lúc ${Utils.formatDateTimeFromSimple(updatedTime)}",
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                modifier = Modifier
                    .constrainAs (txtUpdateTime) {
                        top.linkTo(locationName.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                    },
                fontSize = 12.sp
            )

            IconButton (
                onClick = {
                    Toast.makeText(context, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show()
                    updatedTime = Date()
                    viewModel.reloadData()
                    viewModel.fetchLocation(context)
                    aiViewModel.generateAdvice(Utils.nameList[airQuality.airQuality])
                },
                enabled = airQuality != null,
                content = {
                    Image(
                        painter = painterResource(R.drawable.rotate_right_24),
                        contentDescription = "Reload Icon"
                    )
                },
                modifier = Modifier
                    .constrainAs(reloadIcon) {
                        end.linkTo(parent.end, margin = 12.dp)
                        top.linkTo(txtUpdateTime.top)
                    }
                    .size(20.dp)
            )

            Card(
                modifier = Modifier
                    .constrainAs (tempCard) {
                        top.linkTo(dustCard.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(humidCard.start, margin = 12.dp)
                        width = Dimension.fillToConstraints
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_background)
                )
            ) {
                ConstraintLayout (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val (image, txtLabel, txtContent) = createRefs()
                    Image(
                        painterResource(R.drawable.cloud_sun_24),
                        contentDescription = null,
                        modifier = Modifier
                            .constrainAs(image) {
                                start.linkTo(parent.start, margin = 12.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .size(44.dp)
                    )

                    Text(
                        text = "Nhiệt độ",
                        color = colorResource(R.color.white),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        modifier = Modifier
                            .constrainAs (txtLabel) {
                                end.linkTo(parent.end, margin = 12.dp)
                                top.linkTo(parent.top, margin = 12.dp)
                            }
                    )

                    Text(
                        text = "${airQuality?.temperature ?: "--"}°C",
                        fontSize = 28.sp,
                        color = colorResource(R.color.white),
                        fontFamily = FontFamily(Font(R.font.be_vietnam_medium)),
                        modifier = Modifier
                            .constrainAs(txtContent) {
                                top.linkTo(txtLabel.bottom)
                                end.linkTo(txtLabel.end)
                                start.linkTo(image.end, margin = 12.dp)
                            }
                            .padding(0.dp, 12.dp)
                    )


                }
            }

            // HUMIDITY ----------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .constrainAs (humidCard) {
                        top.linkTo(tempCard.top)
                        start.linkTo(tempCard.end)
                        end.linkTo(parent.end, margin = 12.dp)
                        bottom.linkTo(bottomCardBarrier)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_background)
                )
            ) {
                ConstraintLayout (
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (image, txtLabel, txtContent) = createRefs()
                    Image(
                        painterResource(R.drawable.humidity_24),
                        contentDescription = null,
                        modifier = Modifier
                            .constrainAs(image) {
                                start.linkTo(parent.start, margin = 12.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .size(44.dp)
                    )

                    Text(
                        text = "Độ ẩm",
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        modifier = Modifier
                            .constrainAs(txtLabel) {
                                end.linkTo(parent.end, margin = 12.dp)
                                top.linkTo(parent.top, margin = 12.dp)
                            }
                    )

                    Text(
                        text = "${airQuality?.humidity ?: "--"}%",
                        fontSize = 28.sp,
                        color = colorResource(R.color.white),
                        fontFamily = FontFamily(Font(R.font.be_vietnam_medium)),
                        modifier = Modifier
                            .constrainAs(txtContent) {
                                top.linkTo(txtLabel.bottom)
                                end.linkTo(txtLabel.end)
                                start.linkTo(image.end, margin = 12.dp)
                            }
                            .padding(0.dp, 12.dp)
                    )
                }
            }

            // DUST ------------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .constrainAs(dustCard) {
                        top.linkTo(txtUpdateTime.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                        width = Dimension.fillToConstraints
                    }
                    .padding(0.dp, 20.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                ConstraintLayout (
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (image, txtLabel, txtContent, txtUnit) = createRefs()

                    Text(
                        text = "Chỉ số PM2.5",
                        fontSize = 16.sp,
                        color = colorResource(R.color.white),
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        modifier = Modifier
                            .constrainAs (txtLabel) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                    )

                    Text(
                        text = "${airQuality?.dust ?: "--"}",
                        fontSize = 60.sp,
                        color = colorResource(R.color.white),
                        fontFamily = FontFamily(Font(R.font.be_vietnam_medium)),
                        modifier = Modifier
                            .constrainAs(txtContent) {
                                top.linkTo(txtLabel.bottom)
                                start.linkTo(parent.start)
                            }
                            .padding(0.dp, 12.dp)
                    )
                    Text(
                        text = "µg/m³",
                        fontSize = 16.sp,
                        color = colorResource(R.color.white),
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        modifier = Modifier
                            .constrainAs (txtUnit) {
                                start.linkTo(parent.start)
                                top.linkTo(txtContent.bottom)
                            }
                    )

                    Image(
                        painterResource(R.drawable.dust_pm25),
                        contentDescription = null,
                        modifier = Modifier
                            .constrainAs(image) {
                                end.linkTo(parent.end, margin = 12.dp)
                                top.linkTo(txtLabel.top)
                                bottom.linkTo(txtUnit.bottom)
                            }
                            .size(140.dp),
                        alpha = 0.9f
                    )
                }
            }

            Card (
                modifier = Modifier
                    .constrainAs (airQualityCard) {
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                        top.linkTo(bottomCardBarrier, margin = 12.dp)
                        width = Dimension.fillToConstraints
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_background)
                )
            ){
                ConstraintLayout (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (txtLabel, txtAdvice, qualityIcon, txtAirQuality) = createRefs()


                    if (airQuality.airQuality != 0) {
                        Text(
//                            text = Utils.adviceList[airQuality?.airQuality ?: 0],
                            text = aiAdvice,
                            color = colorResource(R.color.white),
                            fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .constrainAs (txtAdvice) {
                                    start.linkTo(parent.start, margin = 12.dp)
                                    top.linkTo(parent.top, margin = 12.dp)
                                    end.linkTo(qualityIcon.start, margin = 12.dp)
                                    bottom.linkTo(parent.bottom, margin = 12.dp)

                                    width = Dimension.fillToConstraints
                                }
                        )
                        Image(
                            painterResource(R.drawable.wind_24),
                            contentDescription = null,
                            modifier = Modifier
                                .constrainAs(qualityIcon) {
                                    top.linkTo(parent.top, margin = 12.dp)
                                    end.linkTo(parent.end, margin = 12.dp)
                                }
                                .size(96.dp),
                            contentScale = ContentScale.FillBounds
                        )

                        Card (
                            modifier = Modifier
                                .constrainAs (txtAirQuality) {
                                    top.linkTo(qualityIcon.bottom, margin = 12.dp)
                                    start.linkTo(qualityIcon.start)
                                    end.linkTo(qualityIcon.end)
                                    bottom.linkTo(parent.bottom, margin = 12.dp)
                                    width = Dimension.preferredWrapContent
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFFFFF)
                            ),
                            border = BorderStroke(1.dp, Color(Utils.colorList[airQuality?.airQuality ?: 0]))
                        ){
                            Text(
                                text = Utils.nameList[airQuality.airQuality],
                                fontFamily = FontFamily(Font(R.font.be_vietnam_medium)),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = Color(Utils.colorList[airQuality.airQuality ]),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = "Dự doán trong vài giờ tiếp theo",
                fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                fontSize = 14.sp,
                color = colorResource(R.color.white),
                modifier = Modifier.constrainAs (txtPredictFuture) {
                    start.linkTo(parent.start, margin = 12.dp)
                    top.linkTo(airQualityCard.bottom, margin = 12.dp)
                }
            )

            Row (
                modifier = Modifier
                    .constrainAs(cardRow) {
                        top.linkTo(txtPredictFuture.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)

                    }
                    .padding(12.dp, 0.dp)
                    .horizontalScroll(
                        rememberScrollState(),
                        reverseScrolling = true
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            )
            {
                prediction?.let {
                    RenderPredictCard(prediction)
                }
            }

            Card (
                modifier = Modifier
                    .constrainAs(airQualityHistoryChart) {
                        top.linkTo(cardRow.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                    }
                    .fillMaxWidth()
                    .padding(12.dp, 0.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_background)
                )
            ) {
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Lịch sử về chất lượng không khí trong ngày (µg/m³)",
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(12.dp, 12.dp)
                    )
                    if (airQualityList != null && airQualityList.isNotEmpty()) {
                        DustChart(airQualityList)
                    }
                }
            }

            Card (
                modifier = Modifier
                    .constrainAs(tempHistoryChart) {
                        top.linkTo(airQualityHistoryChart.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                    }
                    .fillMaxWidth()
                    .padding(12.dp, 0.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_background)
                )
            ) {
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Lịch sử về nhiệt độ trong ngày (°C)",
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(12.dp, 12.dp)
                    )
                    if (airQualityList != null && airQualityList.isNotEmpty()) {
                        TemperatureChart(airQualityList)
                    }

                }
            }

            Card (
                modifier = Modifier
                    .constrainAs(humidHistoryChart) {
                        top.linkTo(tempHistoryChart.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                    }
                    .fillMaxWidth()
                    .padding(12.dp, 0.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_background)
                )
            ) {
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Lịch sử về độ ẩm trong ngày (%)",
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(12.dp, 12.dp)
                    )
                    if (airQualityList != null && airQualityList.isNotEmpty()) {
                        HumidityChart(airQualityList)
                    }
                }
            }
        }
    }
}