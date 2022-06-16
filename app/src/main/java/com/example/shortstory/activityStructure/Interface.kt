package com.example.shortstory.activityStructure

import com.google.gson.annotations.SerializedName

enum class Interface {
    @SerializedName("title")
    TITLE,
    @SerializedName("character")
    CHARACTER,
    @SerializedName("buttonOption1")
    BUTTON_OPTION1,
    @SerializedName("buttonOption2")
    BUTTON_OPTION2,
    @SerializedName("buttonOption3")
    BUTTON_OPTION3,
    @SerializedName("question")
    QUESTION,
    @SerializedName("setName")
    SET_NAME,
}