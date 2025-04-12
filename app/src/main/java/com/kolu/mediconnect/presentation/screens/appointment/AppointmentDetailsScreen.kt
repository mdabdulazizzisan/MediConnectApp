package com.kolu.mediconnect.presentation.screens.appointment

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolu.mediconnect.domain.model.Appointment
import com.kolu.mediconnect.domain.model.AppointmentStatus

@Composable
fun AppointmentDetailsScreen(
    modifier: Modifier = Modifier,
    appointmentViewModel: AppointmentViewModel,
    appointmentId: String,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(appointmentId) {
        appointmentViewModel.getAppointment(appointmentId = appointmentId)
    }
    val appointment = appointmentViewModel.appointment.collectAsState().value

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Appointment") },
            text = { Text("Are you sure you want to delete this appointment? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        appointment?.let {
                            appointmentViewModel.deleteAppointment(
                                appointment = it,
                                onSuccess = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    onNavigateToHome()
                                },
                                onFailure = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            AppointmentContent(appointment)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show delete button if appointment is not SCHEDULED or VISITED
        appointment?.let {
            if (it.status != AppointmentStatus.SCHEDULED && it.status != AppointmentStatus.VISITED) {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete Appointment", modifier = Modifier.padding(8.dp))
                }
            }
        }

        // Navigation buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Home", modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onNavigateToProfile,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Profile", modifier = Modifier.padding(8.dp))
            }
        }
    }
    BackHandler {
        onBackPressed()
    }
}

@Composable
private fun AppointmentContent(appointment: Appointment?) {
    appointment?.let {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Appointment Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Status card
            StatusCard(status = appointment.status)

            Spacer(modifier = Modifier.height(16.dp))

            // Doctor info
            DetailCard(
                title = "Doctor",
                icon = Icons.Default.Person,
                content = {
                    Text(text = appointment.doctorName, fontSize = 16.sp)
                    Text(text = appointment.division, fontSize = 14.sp, color = Color.Gray)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Hospital info
            DetailCard(
                title = "Hospital",
                icon = Icons.Default.LocationOn,
                content = {
                    Text(text = appointment.hospital, fontSize = 16.sp)
                    Text(
                        text = "${appointment.thana}, ${appointment.district}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date and time info
            Row(modifier = Modifier.fillMaxWidth()) {
                DetailCard(
                    title = "Date",
                    icon = Icons.Default.CalendarToday,
                    content = {
                        Text(text = appointment.date, fontSize = 16.sp)
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                DetailCard(
                    title = "Time",
                    icon = Icons.Default.AccessTime,
                    content = {
                        Text(text = appointment.time, fontSize = 16.sp)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Symptoms info
            DetailCard(
                title = "Symptoms",
                icon = null,
                content = {
                    Text(text = appointment.symptoms, fontSize = 16.sp)
                }
            )
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun StatusCard(status: AppointmentStatus) {
    val (backgroundColor, textColor) = when (status) {
        AppointmentStatus.PENDING_APPROVAL -> Pair(Color(0xFFFFF9C4), Color(0xFFAFA000))
        AppointmentStatus.SCHEDULED -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        AppointmentStatus.REJECTED -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        AppointmentStatus.RESCHEDULED -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        AppointmentStatus.VISITED -> Pair(Color(0xFFE0F7FA), Color(0xFF00796B))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Status: ${status.name.replace('_', ' ')}",
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun DetailCard(
    title: String,
    icon: ImageVector?,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            content()
        }
    }
}

