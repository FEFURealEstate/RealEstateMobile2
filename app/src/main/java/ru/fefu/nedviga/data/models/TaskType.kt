package ru.fefu.nedviga.data.models

import androidx.compose.ui.graphics.Color
import ru.fefu.nedviga.ui.theme.CallingTypeColor
import ru.fefu.nedviga.ui.theme.MeetingTypeColor
import ru.fefu.nedviga.ui.theme.NoneTypeColor
import ru.fefu.nedviga.ui.theme.ShowingTypeColor

enum class TaskType(val color: Color) {
    MEETING(MeetingTypeColor),
    SHOWING(ShowingTypeColor),
    CALLING(CallingTypeColor),
    NONE(NoneTypeColor)
}