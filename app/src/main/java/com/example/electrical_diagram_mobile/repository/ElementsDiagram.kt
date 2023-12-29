package com.example.electrical_diagram_mobile.repository

import com.example.electrical_diagram_mobile.data.ElementData

// репозиторий элементов диаграммы
class ElementsDiagram {
    private val elements = mutableListOf<ElementData>()

    // получить все элементы
    fun getAllElements(): List<ElementData>{
        return elements
    }

    // добавить элемент
    fun addElement(element: ElementData){
        elements.add(element)
    }
}