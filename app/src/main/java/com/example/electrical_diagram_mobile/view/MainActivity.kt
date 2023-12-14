package com.example.electrical_diagram_mobile.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.electrical_diagram_mobile.data.Diagram
import com.example.electrical_diagram_mobile.ui.theme.Electrical_diagram_mobileTheme
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    //объект схемы
    var diagram: Diagram? = null
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Electrical_diagram_mobileTheme {
                // открытие окна создания схемы
                val openDialog = remember { mutableStateOf(true) }
                if(openDialog.value){
                    var name by remember{mutableStateOf("")}
                    var x by remember{mutableStateOf("")}
                    var y by remember{mutableStateOf("")}
                    AlertDialog(
                        onDismissRequest = {
                        },
                        title = {
                            Row(
                                Modifier.fillMaxWidth(),
                                Arrangement.Center
                            ){
                                Text("Создать схему")
                            }},
                        text = {
                            Column(){
                                TextField(
                                    value = name,
                                    onValueChange = {name = it},
                                    label={Text("Название")},
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.padding(10.dp))
                                Row(Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically) {
                                    TextField(
                                        value = x,
                                        onValueChange = {x = it},
                                        label={Text("Ширина")},
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).padding(10.dp))
                                    TextField(
                                        value = y,
                                        onValueChange = {y = it},
                                        label={Text("Длина")},
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).padding(10.dp)
                                    )
                                }
                            }},
                        confirmButton = {
                            Row(
                                Modifier.fillMaxWidth(),
                                Arrangement.Center
                            ){
                                TextButton(
                                    onClick = {
                                        //создание объекта схемы
                                        try {
                                            diagram = Diagram(name, x.toInt(), y.toInt())
                                            openDialog.value = false
                                        }catch (_: Exception){ }
                                    }) {
                                    Text("Создать")
                                }
                            }
                        }
                    )}
            }
        }
    }
}


