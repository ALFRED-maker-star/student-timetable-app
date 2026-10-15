package com.example.data.model

import java.util.Locale

data class Course(
    val id: Long = 0,
    val name: String,
    val code: String,
    val lecturer: String,
    val lecturerEmail: String = "",
    val lecturerOffice: String = "",
    val classroom: String,
    val days: List<AppDayOfWeek>,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val colorHex: String,
    val recurrence: String = "Weekly", // "Weekly", "Bi-weekly", "One-time"
    val notes: String = ""
) {
    val startMinutesOfDay: Int
        get() = startHour * 60 + startMinute

    val endMinutesOfDay: Int
        get() = endHour * 60 + endMinute

    val durationMinutes: Int
        get() = (endMinutesOfDay - startMinutesOfDay).coerceAtLeast(15)

    fun formattedStartTime(): String {
        return formatTime(startHour, startMinute)
    }

    fun formattedEndTime(): String {
        return formatTime(endHour, endMinute)
    }

    fun formattedTimeRange(): String {
        return "${formattedStartTime()} - ${formattedEndTime()}"
    }

    companion object {
        fun formatTime(hour: Int, minute: Int): String {
            return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
    }
}
