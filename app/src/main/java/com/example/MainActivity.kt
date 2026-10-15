package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppDayOfWeek
import com.example.ui.components.ClassMateBottomNavBar
import com.example.ui.components.DirectionsDialog
import com.example.ui.screens.AddEditCourseScreen
import com.example.ui.screens.CourseDetailScreen
import com.example.ui.screens.DailyViewScreen
import com.example.ui.screens.LecturerListScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WeeklyGridScreen
import com.example.ui.theme.ClassMateTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.TimetableViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TimetableViewModel = viewModel()
            val settings by viewModel.settings.collectAsStateWithLifecycle()

            ClassMateTheme(themeMode = settings.themeMode) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: TimetableViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val courses by viewModel.courses.collectAsStateWithLifecycle()
    val lecturers by viewModel.lecturers.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()
    val lecturerSearchQuery by viewModel.lecturerSearchQuery.collectAsStateWithLifecycle()
    val directionsCourse by viewModel.directionsCourse.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.clearSnackbar()
        }
    }

    // Handle system back navigation
    val canGoBack = currentScreen !is Screen.WeeklyGrid
    BackHandler(enabled = canGoBack) {
        viewModel.goBack()
    }

    // Show directions dialog if requested
    directionsCourse?.let { course ->
        DirectionsDialog(
            course = course,
            onDismiss = { viewModel.dismissDirections() }
        )
    }

    val showBottomBar = when (currentScreen) {
        is Screen.WeeklyGrid,
        is Screen.DailyView,
        is Screen.LecturerList,
        is Screen.Settings -> true
        else -> false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                ClassMateBottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.WeeklyGrid -> {
                    WeeklyGridScreen(
                        courses = courses,
                        todayDayOfWeek = viewModel.todayDayOfWeek,
                        weekStartDay = settings.weekStartDay,
                        onDayClick = { day -> viewModel.jumpToDailyView(day) },
                        onCourseClick = { courseId ->
                            viewModel.navigateTo(Screen.CourseDetail(courseId))
                        },
                        onAddCourseClick = {
                            viewModel.navigateTo(Screen.AddEditCourse())
                        }
                    )
                }

                is Screen.DailyView -> {
                    DailyViewScreen(
                        selectedDay = selectedDay,
                        todayDayOfWeek = viewModel.todayDayOfWeek,
                        weekStartDay = settings.weekStartDay,
                        courses = courses,
                        onDaySelected = { day -> viewModel.selectDay(day) },
                        onCourseClick = { courseId ->
                            viewModel.navigateTo(Screen.CourseDetail(courseId))
                        },
                        onGetDirectionsClick = { course ->
                            viewModel.showDirections(course)
                        },
                        onNotifyMeClick = { course ->
                            viewModel.requestNotifyMe(course)
                        },
                        onAddCourseClick = {
                            viewModel.navigateTo(Screen.AddEditCourse())
                        }
                    )
                }

                is Screen.AddEditCourse -> {
                    val courseToEdit = screen.courseId?.let { id ->
                        courses.firstOrNull { it.id == id }
                    }
                    val existingLecturerNames = lecturers.map { it.name }

                    AddEditCourseScreen(
                        courseToEdit = courseToEdit,
                        existingLecturers = existingLecturerNames,
                        onSaveCourse = { course -> viewModel.saveCourse(course) },
                        onDeleteCourse = if (courseToEdit != null) {
                            { id -> viewModel.deleteCourse(id) }
                        } else null,
                        onBack = { viewModel.goBack() }
                    )
                }

                is Screen.CourseDetail -> {
                    val course = courses.firstOrNull { it.id == screen.courseId }

                    CourseDetailScreen(
                        course = course,
                        onBack = { viewModel.goBack() },
                        onEditClick = { id ->
                            viewModel.navigateTo(Screen.AddEditCourse(courseId = id))
                        },
                        onDeleteClick = { id ->
                            viewModel.deleteCourse(id)
                        },
                        onDirectionsClick = { c ->
                            viewModel.showDirections(c)
                        },
                        onNotifyClick = { c ->
                            viewModel.requestNotifyMe(c)
                        }
                    )
                }

                is Screen.LecturerList -> {
                    LecturerListScreen(
                        lecturers = lecturers,
                        searchQuery = lecturerSearchQuery,
                        onSearchQueryChange = { query -> viewModel.setLecturerSearch(query) },
                        onCourseClick = { courseId ->
                            viewModel.navigateTo(Screen.CourseDetail(courseId))
                        }
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        settings = settings,
                        courses = courses,
                        onReminderLeadChange = { mins -> viewModel.updateReminderLeadMinutes(mins) },
                        onWeekStartDayChange = { startDay -> viewModel.updateWeekStartDay(startDay) },
                        onThemeModeChange = { mode -> viewModel.updateThemeMode(mode) },
                        onResetDemoData = { viewModel.resetDemoData() }
                    )
                }
            }
        }
    }
}
