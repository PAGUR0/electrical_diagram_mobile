package com.example.electrical_diagram_mobile.data


// Интерфейс классов данных всех элементов
abstract class ElementData(open var id:String, open var name: String, open val icon: Int) {
    abstract var positionX: Int
    abstract var positionY: Int
    abstract var rotation: Int
    abstract val links: MutableSet<WireData>

    abstract fun copyWith(
        id: String = this.id,
        name: String = this.name,
        icon: Int = this.icon,
        positionX: Int = this.positionX,
        positionY: Int = this.positionY,
        rotation: Int = this.rotation,
        links: MutableSet<WireData> = this.links.toMutableSet()
    ): ElementData
}