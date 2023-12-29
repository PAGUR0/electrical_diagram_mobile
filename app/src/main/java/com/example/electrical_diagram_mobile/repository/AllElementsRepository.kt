package com.example.electrical_diagram_mobile.repository

import com.example.electrical_diagram_mobile.R
import com.example.electrical_diagram_mobile.data.ElementData
import com.example.electrical_diagram_mobile.data.SocketData
import com.example.electrical_diagram_mobile.data.SwitchData

// репозиторий элементов меню
class AllElementsRepository {
    private val elements = mutableSetOf(
        // базовые элементы
        SocketData("SocBiOpen", "Розетка двуполюсная", R.drawable.socket),
        SwitchData("OneSwitchOpen", "Одноклавишный выключатель", R.drawable.switch_one)
    )

    // возврат всех элементов
    fun getAllElements(): Set<ElementData>{
        return elements.toSet()
    }
}