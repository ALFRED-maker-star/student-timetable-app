package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.local.ClassMateDatabase
import com.example.data.local.CourseEntity
import com.example.data.local.SampleData
import com.example.data.model.AppSettings
import com.example.data.model.Course
import com.example.data.model.Lecturer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TimetableRepository(private val context: Context) {
    private val database = ClassMateDatabase.getDatabase(context)
    private val courseDao = database.courseDao()
    private val prefs: SharedPreferences =
        context.getSharedPreferences("classmate_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (courseDao.getCourseCount() == 0) {
                resetToSampleData()
            }
        }
    }

    val courses: Flow<List<Course>> = courseDao.getAllCourses().map { entities ->
        entities.map { it.toDomain() }
    }

    val lecturers: Flow<List<Lecturer>> = courses.map { courseList ->
        courseList.groupBy { it.lecturer.trim() }
            .filterKeys { it.isNotBlank() }
            .map { (name, taughtCourses) ->
                val primary = taughtCourses.first()
                Lecturer(
                    name = name,
                    email = primary.lecturerEmail.ifBlank { "${name.lowercase().replace(" ", ".")}@university.edu" },
                    office = primary.lecturerOffice.ifBlank { "Faculty Hall 201" },
                    courses = taughtCourses
                )
            }
            .sortedBy { it.name }
    }

    fun getCourseById(id: Long): Flow<Course?> {
        return courseDao.getCourseById(id).map { it?.toDomain() }
    }

    suspend fun insertCourse(course: Course): Long {
        return courseDao.insertCourse(CourseEntity.fromDomain(course))
    }

    suspend fun updateCourse(course: Course) {
        courseDao.updateCourse(CourseEntity.fromDomain(course))
    }

    suspend fun deleteCourse(id: Long) {
        courseDao.deleteCourseById(id)
    }

    suspend fun resetToSampleData() {
        courseDao.clearAll()
        val entities = SampleData.sampleCourses.map { CourseEntity.fromDomain(it) }
        courseDao.insertAll(entities)
    }

    private fun loadSettings(): AppSettings {
        return AppSettings(
            reminderLeadMinutes = prefs.getInt("reminder_lead_minutes", 10),
            weekStartDay = prefs.getString("week_start_day", "MON") ?: "MON",
            themeMode = prefs.getString("theme_mode", "LIGHT") ?: "LIGHT"
        )
    }

    fun updateSettings(settings: AppSettings) {
        prefs.edit()
            .putInt("reminder_lead_minutes", settings.reminderLeadMinutes)
            .putString("week_start_day", settings.weekStartDay)
            .putString("theme_mode", settings.themeMode)
            .apply()
        _settings.value = settings
    }
}
