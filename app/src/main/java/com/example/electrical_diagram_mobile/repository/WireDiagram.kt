package com.example.electrical_diagram_mobile.repository

import com.example.electrical_diagram_mobile.data.WireData

// репозиторий всех проводов
class WireDiagram {
    private val wires = mutableSetOf<WireData>()

    // возвратить все провода
    fun getAllWires(): Set<WireData>{
        return wires.toSet()
    }

    // добавить провод
    fun addWire(wire: WireData){
        wire.element1.links.add(wire)
        wire.element2.links.add(wire)
        wires.add(wire)
    }

    // удалить провод
    fun deleteWire(wire: WireData) {
        wire.element1.links.remove(wire)
        wire.element1.links.remove(wire)
        wires.remove(wire)
    }
}