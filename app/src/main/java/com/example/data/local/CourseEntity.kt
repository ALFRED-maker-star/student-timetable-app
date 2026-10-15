package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AppDayOfWeek
import com.example.data.model.Course

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val code: String,
    val lecturer: String,
    val lecturerEmail: String,
    val lecturerOffice: String,
    val classroom: String,
    val daysString: String, // Comma-separated: "MON,WED,FRI"
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val colorHex: String,
    val recurrence: String,
    val notes: String
) {
    fun toDomain(): Course {
        val daysList = daysString.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { AppDayOfWeek.fromShortName(it) }

        return Course(
            id = id,
            name = name,
            code = code,
            lecturer = lecturer,
            lecturerEmail = lecturerEmail,
            lecturerOffice = lecturerOffice,
            classroom = classroom,
            days = if (daysList.isEmpty()) listOf(AppDayOfWeek.MON) else daysList,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            colorHex = colorHex,
            recurrence = recurrence,
            notes = notes
        )
    }

    companion object {
        fun fromDomain(course: Course): CourseEntity {
            return CourseEntity(
                id = course.id,
                name = course.name,
                code = course.code,
                lecturer = course.lecturer,
                lecturerEmail = course.lecturerEmail,
                lecturerOffice = course.lecturerOffice,
                classroom = course.classroom,
                daysString = course.days.joinToString(",") { it.shortName },
                startHour = course.startHour,
                startMinute = course.startMinute,
                endHour = course.endHour,
                endMinute = course.endMinute,
                colorHex = course.colorHex,
                recurrence = course.recurrence,
                notes = course.notes
            )
        }
    }
}
