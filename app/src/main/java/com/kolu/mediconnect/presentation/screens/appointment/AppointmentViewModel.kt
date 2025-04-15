package com.kolu.mediconnect.presentation.screens.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolu.mediconnect.data.repository.AppointmentRepo
import com.kolu.mediconnect.domain.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val appointmentRepo: AppointmentRepo
) : ViewModel() {
    private val _appointment = MutableStateFlow(Appointment())
    val appointment = _appointment.asStateFlow()

    private val _allAppointment = MutableStateFlow(emptyList<Appointment>())
    val allAppointment = _allAppointment.asStateFlow()

    fun updateAppointment(updatedAppointment: Appointment) {
        _appointment.value = updatedAppointment
    }

    fun updateDivision(division: String) {
        _appointment.value = _appointment.value.copy(division = division)
    }

    fun updateDistrict(district: String) {
        _appointment.value = _appointment.value.copy(district = district)
    }

    fun updateThana(thana: String) {
        _appointment.value = _appointment.value.copy(thana = thana)
    }

    fun updateHospital(hospital: String) {
        _appointment.value = _appointment.value.copy(hospital = hospital)
    }

    fun updateDoctorName(doctorName: String) {
        _appointment.value = _appointment.value.copy(doctorName = doctorName)
    }

    fun updateDate(date: String) {
        _appointment.value = _appointment.value.copy(date = date)
    }

    fun updateTime(time: String) {
        _appointment.value = _appointment.value.copy(time = time)
    }

    fun updateSymptoms(symptoms: String) {
        _appointment.value = _appointment.value.copy(symptoms = symptoms)
    }

    fun bookAppointment(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val appointmentId = appointmentRepo.bookAppointment(_appointment.value)
                if (appointmentId != null) {
                    if (appointmentId.isNotEmpty()) {
                        onSuccess(appointmentId)
                    } else {
                        onFailure("Failed to book appointment")
                    }
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Something went wrong")
            }
        }
    }

    fun getAppointment(appointmentId: String = _appointment.value.id): Appointment {
        viewModelScope.launch {
            _appointment.value = appointmentRepo.getAppointmentById(appointmentId)!!
        }
        return _appointment.value
    }

    fun getAllAppointments(): List<Appointment> {
        viewModelScope.launch {
            _allAppointment.value = appointmentRepo.getAllAppointment()
        }
        return allAppointment.value
    }

    fun deleteAppointment(
        appointment: Appointment,
        onSuccess: (String) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val isDeleted = appointmentRepo.deleteAppointment(appointment)
                if (isDeleted) {
                    onSuccess("Appointment deleted successfully")
                } else {
                    onFailure("Failed to delete appointment")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "An error occurred while deleting the appointment")
            }
        }
    }
}
