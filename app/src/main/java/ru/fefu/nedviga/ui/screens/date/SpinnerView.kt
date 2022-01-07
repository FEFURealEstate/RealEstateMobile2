package ru.fefu.nedviga.ui.screens.date

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class SpinnerViewModel: ViewModel() {
    private val _time = MutableLiveData("")
    private var _converted = MutableLiveData(1641038400)
    var time: LiveData<String> = _time
    var converted: LiveData<Int> = _converted

    fun selectDateTime(context: Context) {
        var time = ""
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                val monthStr: String
                val hourStr: String
                val minuteStr: String
                if ((month + 1).toString().length == 1) {
                    monthStr = "0${month + 1}"
                } else if (month + 1 == 12) {
                    monthStr = "${month + 1}"
                } else {
                    monthStr = (month + 1).toString()
                }
                if (hour.toString().length == 1) {
                    hourStr = "0${hour}"
                } else {
                    hourStr = hour.toString()
                }
                if (minute.toString().length == 1) {
                    minuteStr = "0${minute}"
                } else {
                    minuteStr = minute.toString()
                }
                time = "$day - $monthStr - $year $hourStr:$minuteStr"
                val seconds = LocalDateTime.of(year, month + 1, day, hour, minute)
                val sec = seconds.atZone(ZoneId.systemDefault()).toEpochSecond().toInt()
                updateDateTime(time, sec)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun updateDateTime(dateTime: String, converted: Int) {
        _time.value = dateTime
        _converted.value = converted
    }
}