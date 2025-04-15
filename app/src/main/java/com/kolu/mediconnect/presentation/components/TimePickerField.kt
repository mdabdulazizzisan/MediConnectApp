package com.kolu.mediconnect.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    modifier: Modifier = Modifier,
    placeholder: String = "Select Time",
    selectedTime: String = "",
    onTimeSelected: (String) -> Unit,
    is24Hour: Boolean = false
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var displayedTime by remember { mutableStateOf(selectedTime) }

    // Format based on 24 hour or 12 hour preference
    val timeFormatter = if (is24Hour) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("hh:mm a")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
    ) {
        OutlinedTextField(
            value = displayedTime,
            onValueChange = { /* Read-only */ },
            readOnly = true,
            label = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Select time"
                )
            },
            enabled = false
        )
    }

    if (showTimePicker) {
        val initialHour = if (selectedTime.isNotEmpty()) {
            try {
                // Try to parse the existing time
                if (is24Hour) {
                    LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("HH:mm")).hour
                } else {
                    // Handle AM/PM format
                    LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("hh:mm a")).hour
                }
            } catch (e: Exception) {
                LocalTime.now().hour
            }
        } else {
            LocalTime.now().hour
        }

        val initialMinute = if (selectedTime.isNotEmpty()) {
            try {
                if (is24Hour) {
                    LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("HH:mm")).minute
                } else {
                    LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("hh:mm a")).minute
                }
            } catch (e: Exception) {
                LocalTime.now().minute
            }
        } else {
            LocalTime.now().minute
        }

        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = is24Hour
        )

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onConfirm = {
                val localTime = if (is24Hour) {
                    LocalTime.of(timePickerState.hour, timePickerState.minute)
                } else {
                    LocalTime.of(timePickerState.hour, timePickerState.minute)
                }
                val formattedTime = localTime.format(timeFormatter)
                displayedTime = formattedTime
                onTimeSelected(formattedTime)
                showTimePicker = false
            },
            title = "Select Time",
            timePickerState = timePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    timePickerState: TimePickerState
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        androidx.compose.material3.Surface(
            shape = androidx.compose.material3.MaterialTheme.shapes.extraLarge
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Use TimeInput for better mobile experience (Material 3 recommendation)
                TimeInput(
                    state = timePickerState,
                    modifier = Modifier.fillMaxWidth()
                )

                androidx.compose.foundation.layout.Row(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}