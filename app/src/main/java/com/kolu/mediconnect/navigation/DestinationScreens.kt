package com.kolu.mediconnect.navigation

import kotlinx.serialization.Serializable

sealed class DestinationScreens {
    @Serializable
    data object Home: DestinationScreens()

    @Serializable
    data object Login: DestinationScreens()

    @Serializable
    data object Register: DestinationScreens()

    @Serializable
    data object AppointmentBooking: DestinationScreens()
    
    @Serializable
    data class AppointmentDetails(val appointmentId: String): DestinationScreens()

    @Serializable
    data object Profile: DestinationScreens()

    @Serializable
    data object AllAppointments: DestinationScreens()
}