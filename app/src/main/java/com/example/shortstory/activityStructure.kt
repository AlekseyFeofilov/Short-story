package com.example.shortstory


import org.json.JSONArray
import org.json.JSONObject

internal class ButtonStructure(val text: String, val id: Int, val color: String)

internal class ActivityStructure(json: JSONObject, id: Int) {
    companion object {
        const val BACKGROUND = "background"
        const val BUTTON_COLOR = "buttonsColor"
        const val INTERFACE = "interface"
        const val INTERFACE_TYPE = "interfaceType"
        const val TITLE = "title"
        const val QUESTION = "question"
        const val CHARACTER = "character"
        const val TEXT = "text"
        const val ID = "id"
        const val BUTTON_OPTION1 = "buttonOption1"
        const val BUTTON_OPTION2 = "buttonOption2"
        const val BUTTON_OPTION3 = "buttonOption3"
    }

    private val activity = json.getJSONObject(id.toString())
    private val buttonsColor = activity.getString(BUTTON_COLOR)
    val background: String = activity.getString(BACKGROUND)
    val components: JSONArray =
        json.getJSONObject(INTERFACE).getJSONArray(activity.getString(INTERFACE_TYPE))

    var title: String? = null
        private set
    var question: String? = null
        private set
    var character: String? = null
        private set
    var buttonOption1: ButtonStructure? = null
        private set
    var buttonOption2: ButtonStructure? = null
        private set
    var buttonOption3: ButtonStructure? = null
        private set

    init {
        for (i in 0 until components.length()) {
            when (val component = components[i].toString()) {
                TITLE -> title = activity.getString(TITLE)
                QUESTION -> question = activity.getString(QUESTION)
                CHARACTER -> character = activity.getString(CHARACTER)
                BUTTON_OPTION1, BUTTON_OPTION2, BUTTON_OPTION3 -> {
                    val buttonInfo = activity.getJSONObject(component)
                    val text = buttonInfo.getString(TEXT)
                    val nextId = buttonInfo.getInt(ID)
                    val button = ButtonStructure(text, nextId, buttonsColor)

                    when (component) {
                        BUTTON_OPTION1 -> buttonOption1 = button
                        BUTTON_OPTION2 -> buttonOption2 = button
                        else -> buttonOption3 = button
                    }
                }
            }
        }
    }
}