package com.finvoraai.personalfinancemanager.finvora.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import finvoraai.composeapp.generated.resources.Manrope_Bold
import finvoraai.composeapp.generated.resources.Manrope_ExtraBold
import finvoraai.composeapp.generated.resources.Manrope_Medium
import finvoraai.composeapp.generated.resources.Manrope_Regular
import finvoraai.composeapp.generated.resources.Manrope_SemiBold
import finvoraai.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun manropeFamilyFont() = FontFamily(
    Font(Res.font.Manrope_Regular, FontWeight.Normal),
    Font(Res.font.Manrope_Medium, FontWeight.Medium),
    Font(Res.font.Manrope_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Manrope_Bold, FontWeight.Bold),
    Font(Res.font.Manrope_ExtraBold, FontWeight.Black)
)

@Composable
fun H0TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 40.sp,
    lineHeight = 50.sp
)

@Composable
fun H1TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 32.sp,
    lineHeight = 40.sp
)

/**
 * Font size 28.sp
 */

@Composable
fun H2TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 28.sp,
    lineHeight = 36.sp
)

/**
 * Font size 24.sp
 */
@Composable
fun H3TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 24.sp,
    lineHeight = 32.sp
)

@Composable
fun H4TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 20.sp,
    lineHeight = 28.sp
)

/**
 * Font size 18.sp
 */
@Composable
fun H5TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 18.sp,
    lineHeight = 24.sp
)

/**
 * Font size 16.sp
 */
@Composable
fun H6TextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 16.sp,
    lineHeight = 24.sp
)

/**
 * Font size 20.sp
 */

@Composable
fun BodyXXLarge() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Medium,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 20.sp,
    lineHeight = 28.sp
)

/**
 * Font size 18.sp
 */
@Composable
fun BodyXLarge() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Medium,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 18.sp,
    lineHeight = 26.sp
)

/**
 * Font size 16.sp
 */
@Composable
fun BodyLarge() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Medium,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 16.sp,
    lineHeight = 24.sp
)

/**
 * Font size 14.sp
 */
@Composable
fun BodyNormal() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Medium,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 14.sp,
    lineHeight = 20.sp
)

/**
 * Font size 12.sp
 */
@Composable
fun BodySmall() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Medium,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 12.sp
)

/**
 * Font size 10.sp
 */
@Composable
fun BodyXSmall() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Normal,
    color = MaterialTheme.colorScheme.onSurface,
    fontSize = 10.sp
)

@Composable
fun LogoTextStyle() = TextStyle(
    fontFamily = manropeFamilyFont(),
    fontWeight = FontWeight.Black,
    fontSize = 44.sp,
    lineHeight = 52.sp,
    textAlign = TextAlign.Center,
    letterSpacing = 2.sp
)
