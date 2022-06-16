package com.example.shortstory

import android.content.res.AssetManager
import java.io.IOException

internal fun loadJSONFromAsset(assets: AssetManager): String? {
    return  try {
        val appearanceInfoByJSON = assets.open(MainActivity.StringResources.JSON_PATH.string)
        val size: Int = appearanceInfoByJSON.available()
        val buffer = ByteArray(size)
        appearanceInfoByJSON.read(buffer)
        appearanceInfoByJSON.close()
        String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}
