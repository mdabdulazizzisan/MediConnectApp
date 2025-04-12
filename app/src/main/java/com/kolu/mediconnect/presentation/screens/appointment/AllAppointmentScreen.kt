package com.kolu.mediconnect.presentation.screens.appointment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kolu.mediconnect.domain.model.Appointment

@Composable
fun AllAppointmentScreen(
    modifier: Modifier = Modifier,
    viewModel: AppointmentViewModel,
    onAppointmentClick: (appointmentId: String) -> Unit
) {
    val context = LocalContext.current
    val appointments by viewModel.allAppointment.collectAsState()

    LaunchedEffect(appointments) {
        viewModel.getAllAppointments()
    }

    LazyColumn {
        items(appointments.size) { index ->
            val appointment = appointments[index]
            AppointmentItem(
                appointment = appointment,
                onClick = {
                    onAppointmentClick(appointment.id)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AppointmentItem(appointment: Appointment, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = appointment.doctorName,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = appointment.hospital,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = " ${appointment.thana}, ${appointment.district}",
                    style = MaterialTheme.typography.bodyMedium,
                )

            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = appointment.date,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = appointment.time,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

    }
}

@Preview
@Composable
private fun AppointmentItemPrev() {
    AppointmentItem(
        appointment = Appointment(
            district = "Dhaka",
            thana = "Dhanmondi",
            id = "1",
            doctorName = "Dr. John Doe",
            hospital = "City Hospital",
            date = "2023-10-01",
            time = "10:00 AM",
            symptoms = "Fever"
        ),
    ) { }
}
