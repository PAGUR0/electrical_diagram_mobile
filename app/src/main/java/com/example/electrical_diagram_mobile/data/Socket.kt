package com.example.electrical_diagram_mobile.data

// Класс розеток
class Socket(var _name: String,  var _positionX: Int, var _positionY: Int) : Element {
    override var name = _name
    override var positionX = _positionX
    override var positionY = _positionY
    override val links = mutableSetOf<Element>()
}