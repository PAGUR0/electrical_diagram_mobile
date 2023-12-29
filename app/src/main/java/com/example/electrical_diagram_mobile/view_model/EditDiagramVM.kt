package com.example.electrical_diagram_mobile.view_model

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.electrical_diagram_mobile.data.DiagramData
import com.example.electrical_diagram_mobile.data.ElementData
import com.example.electrical_diagram_mobile.data.WireData

class EditDiagramVM : ViewModel() {

    /** состояние диалога */
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean>
        get() = _showDialog

    fun openDialog(){
        _showDialog.value = true
    }

    private fun closeDialog(){
        _showDialog.value = false
    }

    /** состояние меню элементов */
    @OptIn(ExperimentalMaterial3Api::class)
    private val _showMenuElement = MutableLiveData<DrawerState>()
    @OptIn(ExperimentalMaterial3Api::class)
    val showMenuElement: LiveData<DrawerState>
        get() = _showMenuElement

    @OptIn(ExperimentalMaterial3Api::class)
    fun openMenuElement(){
        _showMenuElement.value = DrawerState(DrawerValue.Open)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun closeMenuElement(){
        _showMenuElement.value = DrawerState(DrawerValue.Closed)
    }


    /** элементы меню */
    private val _elementsMenu = mutableStateOf<Set<ElementData>>(emptySet())
    val elementsMenu: State<Set<ElementData>>
        get() = _elementsMenu

    fun refreshElementsMenu(){
        _elementsMenu.value = _diagram.value.getElementsMenu()
    }

    /** элементы диаграммы */
    private val _elements = mutableStateOf<List<ElementData>>(emptyList())
    val elements: State<List<ElementData>>
        get() = _elements

    fun refreshElements(){
        _elements.value = diagram.value.getElements().toList()
    }

    fun addElement(element: ElementData){
        _diagram.value.addElement(element)
        refreshElements()
    }

    /** диаграмма */

    private val _diagram = mutableStateOf(DiagramData("", 0, 0))
    private val diagram: State<DiagramData>
        get() = _diagram

    fun getSizeXDiagram(): Int {
        return _diagram.value.sizeX
    }

    fun getSizeYDiagram(): Int {
        return _diagram.value.sizeY
    }


    fun setDiagramData(name: String, sizeX: Int, sizeY: Int){
        _diagram.value = DiagramData(name, sizeX, sizeY)
        closeDialog()
    }

    /** провода */
    private val _wires = mutableStateOf<Set<WireData>>(emptySet())
    val wires: State<Set<WireData>>
        get() = _wires

    fun refreshWires(){
        _wires.value = _diagram.value.getWires()
    }

    fun addWires(wire: WireData){
        _diagram.value.addWires(wire)
        refreshWires()
    }

    fun deleteWires(wire: WireData){
        _diagram.value.deleteWires(wire)
        refreshWires()
    }
}