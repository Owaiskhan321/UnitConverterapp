package com.example.unitconverter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UnitConverter() {
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }

    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }

    var isInputExpanded by remember { mutableStateOf(false) }
    var isOutputExpanded by remember { mutableStateOf(false) }

    var inputConversionFactor by remember { mutableStateOf(1.0) }
    var outputConversionFactor by remember { mutableStateOf(1.0) }

    // 🔹 Corrected Conversion Function
    fun ConvertUnit() {
        val input = inputValue.toDoubleOrNull() ?: 0.0
        val result = ((input * inputConversionFactor / outputConversionFactor) * 100).roundToInt() / 100.0
        outputValue = result.toString()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unit Converter",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                ConvertUnit()
            },
            label = { Text(text = "Enter Value") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownButton(
                label = inputUnit,
                expanded = isInputExpanded,
                onExpandedChange = { isInputExpanded = it },
                onOptionSelected = { unit, factor ->
                    inputUnit = unit
                    inputConversionFactor = factor
                    ConvertUnit()
                },
                modifier = Modifier.weight(1f) // 🔹 Proper alignment
            )
            Spacer(modifier = Modifier.width(16.dp))

            DropdownButton(
                label = outputUnit,
                expanded = isOutputExpanded,
                onExpandedChange = { isOutputExpanded = it },
                onOptionSelected = { unit, factor ->
                    outputUnit = unit
                    outputConversionFactor = factor
                    ConvertUnit()
                },
                modifier = Modifier.weight(1f) // 🔹 Proper alignment
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Result: $outputValue $outputUnit",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DropdownButton(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Button(
            onClick = { onExpandedChange(!expanded) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = label)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            listOf(
                "Centimeters" to 0.01,
                "Meters" to 1.0,
                "Feet" to 0.3048,
                "Millimeters" to 0.001
            ).forEach { (unit, factor) ->
                DropdownMenuItem(
                    text = { Text(text = unit) },
                    onClick = {
                        onExpandedChange(false)
                        onOptionSelected(unit, factor)
                    }
                )
            }
        }
    }
}
