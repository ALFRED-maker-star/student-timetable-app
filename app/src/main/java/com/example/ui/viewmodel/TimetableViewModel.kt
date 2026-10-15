package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppDayOfWeek
import com.example.data.model.AppSettings
import com.example.data.model.Course
import com.example.data.model.Lecturer
import com.example.data.repository.TimetableRepository
import com.example.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class Screen {
    data object WeeklyGrid : Screen()
    data class DailyView(val day: AppDayOfWeek) : Screen()
    data class AddEditCourse(val courseId: Long? = null) : Screen()
    data class CourseDetail(val courseId: Long) : Screen()
    data object LecturerList : Screen()
    data object Settings : Screen()
}

class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TimetableRepository(application)

    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.WeeklyGrid))
    val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.WeeklyGrid)
        .combine(_screenStack) { _, stack -> stack.lastOrNull() ?: Screen.WeeklyGrid }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Screen.WeeklyGrid)

    // Current Day of Week based on system time
    val todayDayOfWeek: AppDayOfWeek = calculateToday()

    private val _selectedDay = MutableStateFlow(todayDayOfWeek)
    val selectedDay: StateFlow<AppDayOfWeek> = _selectedDay.asStateFlow()

    val courses: StateFlow<List<Course>> = repository.courses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lecturers: StateFlow<List<Lecturer>> = repository.lecturers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AppSettings> = repository.settings

    private val _lecturerSearchQuery = MutableStateFlow("")
    val lecturerSearchQuery: StateFlow<String> = _lecturerSearchQuery.asStateFlow()

    private val _directionsCourse = MutableStateFlow<Course?>(null)
    val directionsCourse: StateFlow<Course?> = _directionsCourse.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun navigateTo(screen: Screen) {
        val currentStack = _screenStack.value
        // If navigating to one of the 4 root tabs, replace root
        if (screen is Screen.WeeklyGrid || screen is Screen.LecturerList || screen is Screen.Settings) {
            _screenStack.value = listOf(screen)
        } else if (screen is Screen.DailyView) {
            _selectedDay.value = screen.day
            _screenStack.value = listOf(screen)
        } else {
            _screenStack.value = currentStack + screen
        }
    }

    fun jumpToDailyView(day: AppDayOfWeek) {
        _selectedDay.value = day
        _screenStack.value = listOf(Screen.DailyView(day))
    }

    fun selectDay(day: AppDayOfWeek) {
        _selectedDay.value = day
    }

    fun goBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            return true
        } else if (current.firstOrNull() !is Screen.WeeklyGrid) {
            _screenStack.value = listOf(Screen.WeeklyGrid)
            return true
        }
        return false
    }

    fun setLecturerSearch(query: String) {
        _lecturerSearchQuery.value = query
    }

    fun showDirections(course: Course) {
        _directionsCourse.value = course
    }

    fun dismissDirections() {
        _directionsCourse.value = null
    }

    fun requestNotifyMe(course: Course) {
        val lead = settings.value.reminderLeadMinutes
        NotificationHelper.showClassReminder(getApplication(), course, lead)
        _snackbarMessage.value = "Reminder set: ${course.code} (${lead} min before ${course.formattedStartTime()})"
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun saveCourse(course: Course) {
        viewModelScope.launch {
            if (course.id == 0L) {
                repository.insertCourse(course)
                _snackbarMessage.value = "Created course: ${course.code}"
            } else {
                repository.updateCourse(course)
                _snackbarMessage.value = "Updated course: ${course.code}"
            }
            goBack()
        }
    }

    fun deleteCourse(courseId: Long) {
        viewModelScope.launch {
            repository.deleteCourse(courseId)
            _snackbarMessage.value = "Course removed from schedule"
            // If currently on CourseDetail, pop back
            if (currentScreen.value is Screen.CourseDetail) {
                goBack()
            }
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetToSampleData()
            _snackbarMessage.value = "Restored university sample schedule"
        }
    }

    fun updateReminderLeadMinutes(minutes: Int) {
        repository.updateSettings(settings.value.copy(reminderLeadMinutes = minutes))
        _snackbarMessage.value = "Class reminder set to $minutes minutes before"
    }

    fun updateWeekStartDay(startDay: String) {
        repository.updateSettings(settings.value.copy(weekStartDay = startDay))
    }

    fun updateThemeMode(mode: String) {
        repository.updateSettings(settings.value.copy(themeMode = mode))
    }

    private fun calculateToday(): AppDayOfWeek {
        val cal = Calendar.getInstance()
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> AppDayOfWeek.MON
            Calendar.TUESDAY -> AppDayOfWeek.TUE
            Calendar.WEDNESDAY -> AppDayOfWeek.WED
            Calendar.THURSDAY -> AppDayOfWeek.THU
            Calendar.FRIDAY -> AppDayOfWeek.FRI
            Calendar.SATURDAY -> AppDayOfWeek.SAT
            Calendar.SUNDAY -> AppDayOfWeek.SUN
            else -> AppDayOfWeek.MON
        }
    }
}
