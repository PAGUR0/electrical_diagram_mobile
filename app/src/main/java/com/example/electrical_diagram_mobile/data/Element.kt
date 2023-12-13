package com.example.electrical_diagram_mobile.data

// Интерфейс всех элементов
interface Element {
    var name: String
    var positionX: Int
    var positionY: Int
    val links: MutableSet<Element>

}