package com.example.electrical_diagram_mobile.data

// класс данных розеток
data class SocketData(override var id: String, override var name: String, override val icon: Int) : ElementData(id, name, icon){
    override var positionX: Int = 0
    override var positionY: Int = 0
    override var rotation = 0
    override val links: MutableSet<WireData> = mutableSetOf()
    override fun copyWith(
        id: String,
        name: String,
        icon: Int,
        positionX: Int,
        positionY: Int,
        rotation: Int,
        links: MutableSet<WireData>
    ): ElementData {
        val copiedElement = SocketData(id, name, icon)
        copiedElement.positionX = positionX
        copiedElement.positionY = positionY
        copiedElement.rotation = rotation
        copiedElement.links.addAll(links)
        return copiedElement
    }
}