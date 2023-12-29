package com.example.electrical_diagram_mobile.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.electrical_diagram_mobile.R
import com.example.electrical_diagram_mobile.data.ElementData
import com.example.electrical_diagram_mobile.data.WireData
import com.example.electrical_diagram_mobile.ui.theme.Electrical_diagram_mobileTheme
import com.example.electrical_diagram_mobile.view_model.EditDiagramVM
import kotlin.math.abs
import kotlin.math.roundToInt

@Suppress("NAME_SHADOWING")
class EditDiagramActivity : ComponentActivity() {
    private val viewModel = EditDiagramVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Electrical_diagram_mobileTheme {
                ScreenEditDiagram()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun ScreenEditDiagram(){
        val showDialog: Boolean by viewModel.showDialog.observeAsState(false)
        // открытие диалога создания нового окна
        if (showDialog){ CreateDiagramDialog() }
        // состояние меню
        val showMenuElement: DrawerState by viewModel.showMenuElement.observeAsState(DrawerState(DrawerValue.Closed))
        // основной интерфейс
        ModalNavigationDrawer(
            drawerState = showMenuElement,
            drawerContent = {
                MenuElementView()
            }) {
            Scaffold(
                topBar = {
                    Row {
                        ButtonOpenMenu()
                    }
                },
                content = {
                    // отображение диаграммы
                    if (!showDialog) { DiagramView() }
                },
                bottomBar = { ButtonCreateNewDiagram() }
            )
        }
    }

    /** представление диаграммы */
    @Composable
    fun DiagramView(){
        var zoomState by remember { mutableFloatStateOf(0.5f) }
        val rotationState by remember { mutableFloatStateOf(0f) }
        val offsetState by remember { mutableStateOf(Offset(0f, 0f)) }
        val elements by remember { viewModel.elements }
        LazyColumn(
            Modifier
                .offset(0.dp, 75.dp)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        // ограничение приближения
                        zoomState = (zoomState * zoom).coerceIn(0.1f, 1f)
                    }
                }
        ) {
            item {
                LazyRow {
                    item {
                        Box(
                            Modifier
                                .rotate(rotationState)
                                .offset {
                                    IntOffset(
                                        offsetState.x.roundToInt(),
                                        offsetState.y.roundToInt()
                                    )
                                }
                                .scale(zoomState)
                                .size(
                                    viewModel.getSizeXDiagram() * 100.dp,
                                    viewModel.getSizeYDiagram() * 100.dp
                                )
                                .border(5.dp, Color.Black)
                                .padding()
                        ){
                            LaunchedEffect(Unit){
                                viewModel.refreshElements()
                                viewModel.refreshWires()
                            }
                            viewModel.wires.value.forEach { wire ->
                                DiagramWireView(wire)
                            }
                            elements.forEachIndexed { _, element ->
                                DiagramElementView(element)
                            }
                        }
                    }
                }
            }
        }
    }

    /** соединение в диаграмме */
    @Composable
    fun DiagramWireView(wire: WireData){
        var isPressed by remember { mutableStateOf(false) }

        val sizeY = wire.element2.positionY - wire.element1.positionY
        val sizeX = wire.element2.positionX - wire.element1.positionX

        wire.size = if(sizeX == 0){1} else { abs(sizeX) } + if(sizeY == 0){1} else { abs(sizeY) }

        var x = DpOffset(wire.element1.positionX * 100.dp + 50.dp, wire.element1.positionY * 100.dp + 50.dp)
        var y = DpOffset(wire.element2.positionX * 100.dp + 50.dp, wire.element1.positionY * 100.dp + 50.dp)
        if(sizeX < 0 && sizeY < 0){
            x = DpOffset(wire.element2.positionX * 100.dp + 50.dp, wire.element2.positionY * 100.dp + 50.dp)
            y = DpOffset(wire.element1.positionX * 100.dp + 50.dp, wire.element2.positionY * 100.dp + 50.dp)
        } else if (sizeX < 0){
            x = DpOffset(wire.element2.positionX * 100.dp + 50.dp, wire.element2.positionY * 100.dp + 50.dp)
            y = DpOffset(wire.element1.positionX * 100.dp + 50.dp, wire.element1.positionY * 100.dp + 50.dp)
        } else if (sizeY < 0){
            x = DpOffset(wire.element1.positionX * 100.dp + 50.dp, wire.element1.positionY * 100.dp + 50.dp)
            y = DpOffset(wire.element2.positionX * 100.dp + 50.dp, wire.element2.positionY * 100.dp + 50.dp)
        }

        Box(
            Modifier
                .offset(x.x, x.y)
                .height(5.dp)
                .width(abs(sizeX) * 100.dp)
                .background(Color.Blue)
                .clickable { isPressed = true }
        )
        Box(
            Modifier
                .offset(y.x, y.y)
                .height(abs(sizeY) * 100.dp)
                .width(5.dp)
                .background(Color.Blue)
                .clickable { isPressed = true }
        )

        DropdownMenu(
            expanded = isPressed,
            onDismissRequest = { isPressed = !isPressed }
        ) {
            Row {
                Text((wire.size.toDouble() / 10).toString())
                Image(
                    painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    Modifier
                        .width(50.dp)
                        .clickable { viewModel.deleteWires(wire) }
                )
            }
        }
    }

    /** элемент диаграммы */
    @Composable
    fun DiagramElementView(element: ElementData){
        var isPressed by remember{ mutableStateOf(false) }
        var dialog by remember{ mutableStateOf(false) }
        var offset by remember{ mutableStateOf(DpOffset(element.positionX.times(100.dp),
            element.positionY.times(100.dp)
        ))}
        var rotation by remember{ mutableIntStateOf(element.rotation) }

        Box(
            Modifier
                .offset(offset.x, offset.y)
                .size(100.dp)
                .clickable {
                    isPressed = true
                }
        ) {
            Icon(
                painter = painterResource(element.icon),
                contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .rotate(rotation.toFloat())
                    .pointerInput(Unit) {
                        detectTransformGestures { _, panGesture, _, _ ->
                            val sizeX = viewModel.getSizeXDiagram() - 1
                            val sizeY = viewModel.getSizeYDiagram() - 1
                            if (element.positionX == 0 || element.positionX == sizeX) {
                                element.positionY =
                                    (element.positionY + (panGesture.y / 65).toInt()).coerceIn(
                                        0,
                                        viewModel.getSizeYDiagram() - 1
                                    )
                            }
                            if (element.positionY == 0 || element.positionY == sizeY) {
                                element.positionX =
                                    (element.positionX + (panGesture.x / 65).toInt()).coerceIn(
                                        0,
                                        viewModel.getSizeXDiagram() - 1
                                    )
                            }
                            offset = DpOffset(
                                element.positionX.times(100.dp),
                                element.positionY.times(100.dp)
                            )
                            viewModel.addWires(WireData("h", element, element))
                            viewModel.deleteWires(WireData("h", element, element))
                        }
                    }
            )
        }

        DropdownMenu(
            expanded = isPressed,
            onDismissRequest = { isPressed = !isPressed }
        ) {
            Row {
                Text(element.id, Modifier.width(150.dp))
                Image(
                    painterResource(id = R.drawable.rotate_left),
                    contentDescription = null,
                    Modifier
                        .width(50.dp)
                        .clickable {
                            element.rotation = (element.rotation + 90).coerceIn(-180, 180)
                            rotation = element.rotation
                        })
                Image(
                    painterResource(id = R.drawable.rotate_right),
                    contentDescription = null,
                    Modifier
                        .width(50.dp)
                        .clickable {
                            element.rotation = (element.rotation - 90).coerceIn(-180, 180)
                            rotation = element.rotation
                        })
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = null,
                    Modifier
                        .fillMaxSize()
                        .clickable {
                            dialog = true
                        }
                )
                if (dialog) {
                    AlertDialog(
                        onDismissRequest = { },
                        text = {
                            Column {
                                viewModel.elements.value.forEach { element2 ->
                                    Text(text = element2.id, Modifier.clickable {
                                        if (element != element2) {
                                            viewModel.addWires(
                                                WireData(
                                                    viewModel.wires.value.size.toString(),
                                                    element,
                                                    element2
                                                )
                                            )
                                        }
                                        dialog = false
                                    })
                                }
                            }
                        },
                        confirmButton = { })
                }
            }
        }
    }

    /** диалог создания схемы */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateDiagramDialog(){
        var name by remember { mutableStateOf("") }
        var x by remember { mutableStateOf("") }
        var y by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {},
            title = {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.Center
                ) {
                    Text("Создать схему")
                }
            },
            text = {
                // поля ввода данных
                Column {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Название") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.padding(10.dp)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = x,
                            onValueChange = { x = it },
                            label = { Text("Ширина") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp)
                        )
                        TextField(
                            value = y,
                            onValueChange = { y = it },
                            label = { Text("Длина") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.Center
                ) {
                    // кнопка создания схемы
                    TextButton({ viewModel.setDiagramData(name, (x.toDouble() * 10).toInt(), (y.toDouble() * 10).toInt()) }) {
                        Text("Создать")
                    }
                }
            }
        )
    }

    /** кнопка открытия меню */
    @Composable
    fun ButtonOpenMenu(){
        Box(Modifier.fillMaxSize()) {
            Image(
                painterResource(id = R.drawable.menu),
                contentDescription = null,
                modifier = Modifier
                    .size(75.dp)
                    .padding(10.dp)
                    .clickable {
                        viewModel.openMenuElement()
                    })
        }
    }

    /** кнопка создания новой схемы */
    @Composable
    fun ButtonCreateNewDiagram(){
        Box(Modifier.size(75.dp)){
            Image(
                painter = painterResource(id = R.drawable.add_diagram),
                contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clickable { viewModel.openDialog() }
            )
        }
    }

    /** элемент меню */
    @Composable
    fun MenuElementView(){
        LaunchedEffect(Unit){
            viewModel.refreshElementsMenu()
        }
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .width(200.dp)
                .background(Color.Gray)
        ){
            items(viewModel.elementsMenu.value.size) { id ->
                val element = viewModel.elementsMenu.value.elementAt(id)
                Column(
                    Modifier
                        .clickable {
                            val id = element.id + viewModel.elements.value.size
                            viewModel.addElement(
                                element.copyWith(id)
                            )
                            viewModel.closeMenuElement()
                        }
                        .size(200.dp, 250.dp),
                ) {
                    Icon(
                        painter = painterResource(element.icon),
                        contentDescription = null,
                        Modifier
                            .size(200.dp)
                            .padding(10.dp)
                    )
                    Text(
                        element.name,
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}