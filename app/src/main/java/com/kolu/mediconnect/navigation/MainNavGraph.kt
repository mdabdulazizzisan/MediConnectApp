package com.kolu.mediconnect.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kolu.mediconnect.presentation.screens.appointment.AllAppointmentScreen
import com.kolu.mediconnect.presentation.screens.appointment.AppointmentBookingScreen
import com.kolu.mediconnect.presentation.screens.appointment.AppointmentDetailsScreen
import com.kolu.mediconnect.presentation.screens.appointment.AppointmentViewModel
import com.kolu.mediconnect.presentation.screens.auth.AuthViewModel
import com.kolu.mediconnect.presentation.screens.auth.login.LoginScreen
import com.kolu.mediconnect.presentation.screens.auth.register.RegisterScreen
import com.kolu.mediconnect.presentation.screens.home.HomeScreen
import com.kolu.mediconnect.presentation.screens.user.ProfileScreen
import com.kolu.mediconnect.presentation.screens.user.UserViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val appointmentViewModel: AppointmentViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = DestinationScreens.Home,
        modifier = modifier
    ) {
        composable<DestinationScreens.Home> {
            HomeScreen(
                onNavigateToLoginScreen = {
                    navController.navigate(DestinationScreens.Login)
                },
                onBookAnAppointmentClick = {
                    navController.navigate(DestinationScreens.AppointmentBooking)
                },
                onProfileClick = {
                    navController.navigate(DestinationScreens.Profile)
                }
            )
        }
        composable<DestinationScreens.Login> {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegisterScreen = {
                    navController.navigate(DestinationScreens.Register)
                },
                onLoginSuccess = {
                    navController.navigate(DestinationScreens.Home)
                }
            )
        }
        composable<DestinationScreens.Register> {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = { navController.navigate(DestinationScreens.Login) }
            )
        }
        composable<DestinationScreens.AppointmentBooking> {
            AppointmentBookingScreen(
                modifier = Modifier
                    .fillMaxSize(),
                viewModel = appointmentViewModel,
                onAppointmentSuccess = { appointmentId ->
                    navController.navigate(DestinationScreens.AppointmentDetails(appointmentId)) {
                        popUpTo(DestinationScreens.Home) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<DestinationScreens.AppointmentDetails> {
            val appointmentId = it.arguments?.getString("appointmentId")
            if (appointmentId != null) {
                AppointmentDetailsScreen(
                    modifier = Modifier.fillMaxSize(),
                    appointmentViewModel = appointmentViewModel,
                    appointmentId = appointmentId,
                    onNavigateToHome = {
                        navController.navigate(DestinationScreens.Home) {
                            popUpTo(DestinationScreens.Home) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(DestinationScreens.Profile) {
                            popUpTo(DestinationScreens.Home) {
                                inclusive = true
                            }
                        }
                    },
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Appointment ID is null")
                }
            }
        }
        composable<DestinationScreens.Profile> {
            ProfileScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = userViewModel,
                onAppointmentsClick = {
                    navController.navigate(DestinationScreens.AllAppointments)
                },
                onLogoutClick = {
                    navController.navigate(DestinationScreens.Login) {
                        popUpTo(DestinationScreens.Home) {
                            inclusive = true
                        }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable<DestinationScreens.AllAppointments> {
            AllAppointmentScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = appointmentViewModel,
                onAppointmentClick = { appointmentId ->
                    navController.navigate(DestinationScreens.AppointmentDetails(appointmentId))
                }
            )
        }
    }
}