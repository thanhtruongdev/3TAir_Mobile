package com.example.a3tair.utils

import com.example.a3tair.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    const val BASE_URL = "https://eac4-103-199-69-181.ngrok-free.app/"

    fun formatDateTime(date : Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDateTimeOnlyHour(date : Date): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    val adviceList = arrayOf(
        "Chưa có dữ liệu!",
        "Chất lượng không khí hôm nay rất tốt! Mong rằng bạn sẽ có một ngày thật thoải mái và cùng chung tay bảo vệ môi trường. \uD83D\uDC9A",
        "Không khí ở mức trung bình. Bạn vẫn có thể ra ngoài, nhưng nên hạn chế hoạt động mạnh và nhớ theo dõi chất lượng không khí thường xuyên nhé!",
        "Chất lượng không khí kém! Hạn chế ra ngoài, đeo khẩu trang nếu cần thiết và đóng cửa sổ để bảo vệ sức khỏe nhé!",
        "Chất lượng không khí xấu, nên hạn chế ra ngoài. Đeo khẩu trang nếu cần thiết, giữ nhà kín, uống nhiều nước và theo dõi chỉ số không khí thường xuyên.",
        "Chất lượng không khí rất xấu! Hạn chế ra ngoài, đóng kín cửa, sử dụng máy lọc không khí và luôn đeo khẩu trang nếu bắt buộc phải ra ngoài. Bảo vệ sức khỏe là ưu tiên hàng đầu!",
        "Không khí đang ở mức nguy hại! Hạn chế ra ngoài, luôn đeo khẩu trang. Đóng kín cửa, dùng máy lọc không khí và theo dõi chất lượng không khí thường xuyên để bảo vệ sức khỏe!"
    )

    val colorList = arrayOf(
        0x00B3F90B,
        0xFF00DC7F,
        0xFF70BF8B,
        0xFFFFB72D,
        0xFFF04660,
        0xFFE21B1D,
        0xFFE21B1D
    )

    val backgroundColorList = arrayOf(
        0x0083945E,
        0xFF114831,
        0xFF42594A,
        0xFF604717,
        0xFF5C0412,
        0xFF490001,
        0xFF490001
    )

    val nameList = arrayOf("","Tốt", "Trung bình", "Kém", "Xấu", "Rất xấu", "Nguy hại")

    data class AirQualityData(
        val dust : Double,
        val temperature : Double,
        val humidity : Double
    )
}