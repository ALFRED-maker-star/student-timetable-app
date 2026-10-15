package com.example.data.model

enum class AppDayOfWeek(
    val shortName: String,
    val fullName: String,
    val dayNumber: Int // 1 = Monday ... 7 = Sunday
) {
    MON("Mon", "Monday", 1),
    TUE("Tue", "Tuesday", 2),
    WED("Wed", "Wednesday", 3),
    THU("Thu", "Thursday", 4),
    FRI("Fri", "Friday", 5),
    SAT("Sat", "Saturday", 6),
    SUN("Sun", "Sunday", 7);

    companion object {
        fun fromDayNumber(number: Int): AppDayOfWeek {
            return entries.firstOrNull { it.dayNumber == number } ?: MON
        }

        fun fromShortName(name: String): AppDayOfWeek {
            return entries.firstOrNull { it.shortName.equals(name, ignoreCase = true) }
                ?: entries.firstOrNull { it.fullName.startsWith(name, ignoreCase = true) }
                ?: MON
        }

        fun getWeekDays(startDay: String): List<AppDayOfWeek> {
            return if (startDay.equals("SUN", ignoreCase = true)) {
                listOf(SUN, MON, TUE, WED, THU, FRI, SAT)
            } else {
                listOf(MON, TUE, WED, THU, FRI, SAT, SUN)
            }
        }
    }
}
