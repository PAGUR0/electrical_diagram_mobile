package com.example.electrical_diagram_mobile.data

// класс данных проводов
data class WireData(var id: String, val element1: ElementData ,val element2: ElementData) {
    var size:Int = 0
}