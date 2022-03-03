package com.example.shortstory

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import org.json.JSONObject
import java.io.IOException
import com.example.shortstory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    internal companion object {
        const val ACTIVITY_ID = "activityId"
        const val NAME = "name"
    }

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityId = intent.getIntExtra(ACTIVITY_ID, 0)
        name = intent.getStringExtra(NAME)

        val json = JSONObject(loadJSONFromAsset()!!)
        val activity = json.getJSONObject(activityId.toString())
        interfaceInit(json, activity)
    }

    private fun interfaceInit(json: JSONObject, activity: JSONObject) {
        binding.background.background = getDrawableResource(activity, "background")
        val interfaceType = activity.getString("interfaceType")
        val components = json.getJSONObject("interface").getJSONArray(interfaceType)
        val intent = Intent(this, MainActivity::class.java)

        for (i in 0 until components.length()) {
            when (val component = components[i].toString()) {
                "title" -> setTextView(binding.titleTextView, activity, component)
                "question" -> setTextView(binding.questionTextView, activity, component)
                "setName" -> setSetNameEditText(intent)
                "character" -> setCharacter(activity)
                else -> {
                    val view = activity.getJSONObject(component)
                    val color = activity.getString("buttonsColor");
                    when (component) {
                        "buttonOption1" -> setButtonOption(binding.buttonOption1, view, color, intent)
                        "buttonOption2" -> setButtonOption(binding.buttonOption2, view, color, intent)
                        else -> setButtonOption(binding.buttonOption3, view, color, intent)
                    }
                }
            }
        }
    }

    private fun setButtonOption(button: Button, buttonInformation: JSONObject, buttonColor: String, intent: Intent){
        when(buttonColor){
            "dark" -> button.setBackgroundColor(ContextCompat.getColor(this, R.color.dark))
            "light" -> button.setBackgroundColor(ContextCompat.getColor(this, R.color.light))
        }

        button.setOnClickListener {
            intent.putExtra(MainActivity.ACTIVITY_ID, buttonInformation.getInt("id"))
            startActivity(intent)
        }

        button.text = buttonInformation.getString("text")
        button.visibility = View.VISIBLE
    }

    private fun setTextView(textView: TextView, activity: JSONObject, textViewName: String) {
        textView.text = if (name == null) activity.getString(textViewName)
        else activity.getString(textViewName).format(name)
        textView.visibility = View.VISIBLE
    }

    private fun setSetNameEditText(intent: Intent) {
        binding.buttonOption3.isEnabled = false
        binding.setNameEditText.visibility = View.VISIBLE

        binding.setNameEditText.doAfterTextChanged {
            intent.putExtra(MainActivity.NAME, it.toString())
            binding.buttonOption3.isEnabled = true
        }
    }

    private fun setCharacter(activity: JSONObject) {
        binding.character.background = getDrawableResource(activity,"character")
    }

    private fun getDrawableResource(json: JSONObject, key: String): Drawable? {
        val drawable = json.getString(key)
        val resourceId = resources.getIdentifier(drawable, "drawable", this.packageName)
        return ResourcesCompat.getDrawable(resources, resourceId, null)
    }

    private fun loadJSONFromAsset(): String? {
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
}