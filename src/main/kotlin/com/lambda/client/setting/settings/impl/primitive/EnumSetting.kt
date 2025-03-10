package com.lambda.client.setting.settings.impl.primitive

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.lambda.client.setting.settings.MutableSetting
import com.lambda.commons.extension.next

class EnumSetting<T : Enum<T>>(
    name: String,
    value: T,
    visibility: () -> Boolean = { true },
    consumer: (prev: T, input: T) -> T = { _, input -> input },
    description: String = ""
) : MutableSetting<T>(name, value, visibility, consumer, description) {

    private val enumClass: Class<T> = value.declaringClass
    val enumValues: Array<out T> = enumClass.enumConstants

    fun nextValue() {
        value = value.next()
    }

    override fun setValue(valueIn: String) {
        super.setValue(valueIn.uppercase().replace(' ', '_'))
    }

    override fun write(): JsonElement = JsonPrimitive(value.name)

    override fun read(jsonElement: JsonElement?) {
        jsonElement?.asJsonPrimitive?.asString?.let { element ->
            enumValues.firstOrNull { it.name.equals(element, true) }?.let {
                value = it
            }
        }
    }

}