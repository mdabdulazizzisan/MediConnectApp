package com.kolu.mediconnect.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    items: List<String>,
    placeholder: String,
    selectedItem: String = "",
    onItemSelected: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentValue by remember { mutableStateOf(selectedItem) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showBottomSheet = true }
        ) {
            OutlinedTextField(
                value = currentValue,
                onValueChange = { /* Read only */ },
                readOnly = true,
                label = { Text(placeholder) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select option"
                    )
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = OutlinedTextFieldDefaults.colors().focusedTextColor,
                    disabledContainerColor = OutlinedTextFieldDefaults.colors().focusedContainerColor,
                    disabledBorderColor = OutlinedTextFieldDefaults.colors().focusedTextColor,
                    disabledLabelColor = OutlinedTextFieldDefaults.colors().focusedLabelColor,
                    disabledTrailingIconColor = OutlinedTextFieldDefaults.colors().focusedTrailingIconColor,
                )
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                LazyColumn(
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(items) { item ->
                        ListItem(
                            headlineContent = { Text(item) },
                            modifier = Modifier.clickable {
                                currentValue = item
                                onItemSelected(item)
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}