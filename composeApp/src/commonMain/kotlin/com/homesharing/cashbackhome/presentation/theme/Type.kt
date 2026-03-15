package com.homesharing.cashbackhome.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import homesharing.composeapp.generated.resources.Manrope_Bold
import homesharing.composeapp.generated.resources.Manrope_Medium
import homesharing.composeapp.generated.resources.Manrope_Regular
import homesharing.composeapp.generated.resources.Manrope_SemiBold
import homesharing.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography(): Typography {
    val manropeFontFamily = FontFamily(
        Font(Res.font.Manrope_Regular, FontWeight.Normal),
        Font(Res.font.Manrope_Medium, weight = FontWeight.Medium),
        Font(Res.font.Manrope_SemiBold, weight = FontWeight.SemiBold),
        Font(Res.font.Manrope_Bold, weight = FontWeight.Bold)
    )

    return Typography(
        headlineLarge = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
    )
}
