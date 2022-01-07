package ru.fefu.nedviga.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val AnotherGray = Color(0xFF514F4F)
val DarkGray = Color(0xFF141414)

val MeetingTypeColor = Color(0xFFFF4A6D)
val PresentationTypeColor = Color(0xFFFFF6A5)
val PhoneCallTypeColor = Color(0xFF00D9BB)
val NoneTypeColor = MediumGray

val Colors.topAppBarContentColor: Color
@Composable
get() = if (isLight) Color.White else LightGray

val Colors.topAppBarBackgroundColor: Color
@Composable
get() = if (isLight) Purple500 else Color.Black

val Colors.taskItemBackgroundColor: Color
@Composable
get() = if (isLight) Color.White else DarkGray

val Colors.taskItemTextColor: Color
@Composable
get() = if (isLight) DarkGray else LightGray

val Colors.splashScreenBackground: Color
@Composable
get() = if (isLight) Purple700 else Color.Black

val Colors.fabBackground: Color
@Composable
get() = if (isLight) Purple500 else AnotherGray