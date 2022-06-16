package com.example.shortstory.activityStructure

import com.google.gson.annotations.SerializedName

data class ActivityStructure(
    val backgroundName: String,
    @SerializedName("interface")
    val _interface: List<Interface>,
    val title: String? = null,
    val character: String? = null,
    val question: String? = null,
    val buttonsColor: ButtonColor,
    val buttonOption1: ButtonStructure? = null,
    val buttonOption2: ButtonStructure? = null,
    val buttonOption3: ButtonStructure? = null,
)