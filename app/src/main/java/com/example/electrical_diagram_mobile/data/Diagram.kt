package com.example.electrical_diagram_mobile.data

// Класс схемы
class Diagram(_name: String, _sizeX: Int, _sizeY: Int) {
    private var name: String = _name
    private var sizeX: Int = _sizeX
    private var sizeY: Int = _sizeY
    private var elements: GraphElement = GraphElement()

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getSizeX(): Int {
        return sizeX
    }

    fun setSizeX(sizeX: Int) {
        this.sizeX = sizeX
    }

    fun getSizeY(): Int {
        return sizeY
    }

    fun setSizeY(sizeY: Int) {
        this.sizeY = sizeY
    }
}