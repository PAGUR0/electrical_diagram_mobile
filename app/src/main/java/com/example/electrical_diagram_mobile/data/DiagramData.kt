package com.example.electrical_diagram_mobile.data

import com.example.electrical_diagram_mobile.repository.AllElementsRepository
import com.example.electrical_diagram_mobile.repository.ElementsDiagram
import com.example.electrical_diagram_mobile.repository.WireDiagram


// Класс схемы
data class DiagramData(var name: String,var sizeX: Int,var sizeY: Int) {
    private val elementsMenu = AllElementsRepository()
    private val elements = ElementsDiagram()
    private val wires = WireDiagram()

    fun getElementsMenu(): Set<ElementData> {
        return elementsMenu.getAllElements()
    }

    fun getElements(): List<ElementData> {
        return elements.getAllElements()
    }

    fun getWires(): Set<WireData> {
        return wires.getAllWires()
    }

    fun addWires(wire: WireData){
        wires.addWire(wire)
    }

    fun deleteWires(wire: WireData){
        wires.deleteWire(wire)
    }

    fun addElement(element: ElementData){
        elements.addElement(element)
    }

}