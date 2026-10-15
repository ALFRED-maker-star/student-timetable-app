package com.example.data.model

data class Lecturer(
    val name: String,
    val email: String = "",
    val office: String = "",
    val courses: List<Course> = emptyList()
) {
    val initials: String
        get() {
            val parts = name.trim().split(" ").filter { it.isNotBlank() }
            return when {
                parts.isEmpty() -> "L"
                parts.size == 1 -> parts[0].take(2).uppercase()
                else -> "${parts.first().first()}${parts.last().first()}".uppercase()
            }
        }
}

data class AppSettings(
    val reminderLeadMinutes: Int = 10, // 5, 10, 15
    val weekStartDay: String = "MON",  // "MON", "SUN"
    val themeMode: String = "LIGHT"    // "SYSTEM", "LIGHT", "DARK"
)
