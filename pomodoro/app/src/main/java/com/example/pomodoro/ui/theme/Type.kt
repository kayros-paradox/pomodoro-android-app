package com.example.pomodoro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pomodoro.R

val Play = FontFamily(
    Font(R.font.play_regular, FontWeight.Normal),
    Font(R.font.play_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Play,
        fontWeight = FontWeight.Light,
        fontSize = 22.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Play,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 36.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Play,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Play,
        fontWeight = FontWeight.Thin,
        fontSize = 18.sp,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Play,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Play,
        fontWeight = FontWeight.Thin,
        fontSize = 12.sp,
        lineHeight = 12.sp
    )
)