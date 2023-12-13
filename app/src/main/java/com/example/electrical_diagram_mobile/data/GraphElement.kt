package com.example.electrical_diagram_mobile.data

// Граф элементов
class GraphElement {
    private val elements = mutableMapOf<String, Element>()

    // Добавление элемента
    fun addElement(name: String, element: Element){
        elements[name] = element
    }

    // Создание связи
    fun connect(first: Element, second: Element){
        first.links.add(second)
        second.links.add(first)
    }
}