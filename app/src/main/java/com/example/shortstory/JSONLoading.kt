package com.example.shortstory

import android.content.res.AssetManager
import android.hardware.Camera.open
import android.os.ParcelFileDescriptor.open
import android.system.Os.open
import java.io.IOException
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.AsynchronousServerSocketChannel.open
import java.nio.channels.AsynchronousSocketChannel.open
import java.nio.channels.DatagramChannel.open
import java.nio.channels.FileChannel.open
import java.nio.channels.Pipe.open
import java.nio.channels.Selector.open
import java.nio.channels.ServerSocketChannel.open
import java.nio.channels.SocketChannel.open

internal fun loadJSONFromAsset(assets: AssetManager): String? {
    var json: String? = null

    json = try {
        val appearanceInfoByJSON = assets.open("appearanceInfo.json")
        val size: Int = appearanceInfoByJSON.available()
        val buffer = ByteArray(size)
        appearanceInfoByJSON.read(buffer)
        appearanceInfoByJSON.close()
        String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }

    return json
}
