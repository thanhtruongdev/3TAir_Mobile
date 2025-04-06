package com.example.a3tair

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
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
import androidx.lifecycle.ViewModelProvider
import com.example.a3tair.models.AirQuality
import com.example.a3tair.models.Prediction
import com.example.a3tair.ui.LineChart
import com.example.a3tair.ui.theme._3TAirTheme
import com.example.a3tair.utils.Utils
import com.example.a3tair.viewModel.AirQualityViewModel
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[AirQualityViewModel::class.java]
        setContent {
            _3TAirTheme {
                Scaffold(
                    topBar = { topAppBar() }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        mainView(viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(80.dp)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier.statusBarsPadding()
        )

    }
}

@Composable
fun mainView(viewModel : AirQualityViewModel) {

    val context = LocalContext.current

    var updatedTime by remember {
        mutableStateOf(Date())
    }

    val airQualityList = viewModel.airQuality.observeAsState().value?.toList()

    val airQuality = airQualityList?.get(airQualityList.size - 1) ?: AirQuality(0, Date(), 0.0, 0.0, 0, 0.0)

    val prediction = viewModel.prediction.observeAsState().value?.toList()

    val isLoaded = viewModel.isLoaded

    Box (
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(Color(0xFFa3bded), Color(0xFF6991c7))
//                )
//            )
    ) {
        Image(
            painterResource(R.drawable.background_backup),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize().blur(0.dp),
            contentScale = ContentScale.Crop
        )

        ConstraintLayout (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            val (progress, locationIcon, locationLabel, locationName,
                txtUpdateTime, reloadIcon) = createRefs()

            val (tempCard, humidCard, dustCard, airQualityCard) = createRefs()

            val bottomCardBarrier = createBottomBarrier(tempCard, humidCard)

            val (txtPredictFuture, cardRow) = createRefs()

            val (airQualityHistoryChart, tempHistoryChart, humidHistoryChart, progressIndicator) = createRefs()

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
                text = "TP.Hồ Chí Minh",
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
                text = "Cập nhật lúc ${Utils.formatDateTime(updatedTime)}",
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
                },
                enabled = if(airQuality != null) true else false,
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
                        text = "µm/m3",
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

                    Text(
                        text = Utils.adviceList[airQuality?.airQuality ?: 0],
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
                        painterResource(Utils.iconList[airQuality?.airQuality ?: 0]),
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
                            text = Utils.nameList[airQuality?.airQuality ?: 0],
                            fontFamily = FontFamily(Font(R.font.be_vietnam_medium)),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color(Utils.colorList[airQuality?.airQuality ?: 0]),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        )
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
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            )
            {
                prediction?.let {
                    renderPredictCard(prediction)
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
                        text = "Lịch sử về chất lượng không khí trong ngày",
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(12.dp, 12.dp)
                    )
                    AirQualityChart(airQualityList)
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
                        text = "Lịch sử về nhiệt độ trong ngày",
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(12.dp, 12.dp)
                    )
                    TemperatureChart(airQualityList)
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
                        text = "Lịch sử về độ ẩm trong ngày",
                        fontFamily = FontFamily(Font(R.font.be_vietnam_light)),
                        fontSize = 14.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(12.dp, 12.dp)
                    )
                    HumidityChart(airQualityList)
                }
            }
        }
    }
}

@Composable
fun AirQualityChart(airQuality: List<AirQuality>?) {
    val airQualityList = arrayListOf<Double>()
    airQuality?.let { it ->
        airQuality.forEach { it ->
            airQualityList += it.dust
        }
    }

    if (airQualityList != null && airQualityList.isNotEmpty()) {
        LineChart(
            data = airQualityList,
            label = "PM2.5 | Nhỏ nhất ${airQualityList.minOrNull()} | Lớn nhất ${airQualityList.maxOrNull()}",
            lineColor = listOf(Color(0xFFFFC107), Color(0xFFFFC107)),
            firstGradientFillColor = Color(0xFFFFD557),
            minValue = airQualityList.minOrNull()?.minus(5) ?: 0.0,
            maxValue = airQualityList.maxOrNull()?.plus(5) ?: 0.0,
            dotColor = Color(0xFFE7AD0E)
        )
    }
}

@Composable
fun TemperatureChart(airQuality : List<AirQuality>?) {
    var tempList = arrayListOf<Double>()
    airQuality?.let { it ->
        airQuality.forEach { it ->
            tempList += it.temperature
        }
    }

    if (tempList != null && tempList.isNotEmpty()) {
        Log.i("MINVALUE", tempList.minOrNull().toString())
        Log.i("VALUE", tempList.toString())
        LineChart(
            data = tempList,
            label = "Nhiệt độ | Nhỏ nhất ${tempList.minOrNull()} | Lớn nhất ${tempList.maxOrNull()}",
            lineColor = listOf(Color(0xFFF44336), Color(0xFFF44336)),
            firstGradientFillColor = Color(0xFFFF7D78),
            minValue = tempList.minOrNull()?.minus(5.0) ?: 0.0,
            maxValue = tempList.maxOrNull()?.plus(5.0) ?: 0.0,
            dotColor = Color(0xFFE50900)
        )
    }
}

@Composable
fun HumidityChart(airQuality: List<AirQuality>?) {
    var humidityList = arrayListOf<Double>()
    airQuality?.let { it ->
        airQuality.forEach { it ->
            humidityList += it.humidity
        }
    }

    if (humidityList != null && humidityList.isNotEmpty()) {
        LineChart(
            data = humidityList,
            label = "Độ ẩm | Nhỏ nhất ${humidityList.minOrNull()} | Lớn nhất ${humidityList.maxOrNull()}",
            lineColor = listOf(Color(0xFF2196F3), Color(0xFF2196F3)),
            firstGradientFillColor = Color(0xFF2693EC),
            minValue = humidityList.minOrNull()?.minus(5.0) ?: 0.0,
            maxValue = humidityList.maxOrNull()?.plus(5.0) ?: 0.0,
            dotColor = Color(0xFF0082E7)
        )
    }
}

@Composable
fun renderPredictCard(prediction: List<Prediction>) {
    prediction.forEach { it ->
        PredictCard(it)
    }
}

@Composable
fun PredictCard(prediction: Prediction) {
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
//                    containerColor = Color(Utils.backgroundColorList[prediction.airQuality])
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
                    fontFamily = FontFamily(Font(R.font.be_vietnam_regular)),
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