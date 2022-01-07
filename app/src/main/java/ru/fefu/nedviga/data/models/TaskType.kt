package ru.fefu.nedviga.data.models

import androidx.compose.ui.graphics.Color
import ru.fefu.nedviga.ui.theme.PhoneCallTypeColor
import ru.fefu.nedviga.ui.theme.MeetingTypeColor
import ru.fefu.nedviga.ui.theme.NoneTypeColor
import ru.fefu.nedviga.ui.theme.PresentationTypeColor

enum class TaskType(val color: Color) {
    meeting(MeetingTypeColor),
    presentation(PresentationTypeColor),
    phone_call(PhoneCallTypeColor),
    none(NoneTypeColor)
}