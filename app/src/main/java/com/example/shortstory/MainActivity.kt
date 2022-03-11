package com.example.shortstory

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import org.json.JSONObject
import com.example.shortstory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private companion object {
        const val ACTIVITY_ID = "activityId"
        const val NAME = "name"
        const val DARK = "dark"
        const val LIGHT = "light"
        const val SET_NAME = "setName"
        const val DRAWABLE = "drawable"
    }

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityId = intent.getIntExtra(ACTIVITY_ID, 0)
        name = intent.getStringExtra(NAME)

        val json = JSONObject(loadJSONFromAsset(assets)!!)
        val activity = ActivityStructure(json, activityId)
        interfaceInit(activity)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun interfaceInit(activity: ActivityStructure) {
        binding.backgroundImageView.background = getDrawableResource(activity.background)
        val intent = Intent(this, MainActivity::class.java)

        for (i in 0 until activity.components.length()) {
            when (val component = activity.components[i].toString()) {
                ActivityStructure.TITLE -> setTextView(binding.titleTextView, activity.title!!)
                ActivityStructure.QUESTION -> setTextView(binding.questionTextView, activity.question!!)
                SET_NAME -> setSetNameEditText(intent)
                ActivityStructure.CHARACTER -> setCharacter(activity.character!!)
                else -> {
                    when (component) {
                        ActivityStructure.BUTTON_OPTION1 -> {
                            setButtonOption(binding.buttonOption1, activity.buttonOption1!!, intent)
                        }
                        ActivityStructure.BUTTON_OPTION2 -> {
                            setButtonOption(binding.buttonOption2, activity.buttonOption2!!, intent)
                        }
                        else -> setButtonOption(binding.buttonOption3, activity.buttonOption3!!, intent)
                    }
                }
            }
        }
    }

    private fun setButtonOption(button: Button, buttonInfo: ButtonStructure, intent: Intent){
        when(buttonInfo.color){
            DARK -> button.setBackgroundColor(ContextCompat.getColor(this, R.color.dark))
            LIGHT -> button.setBackgroundColor(ContextCompat.getColor(this, R.color.light))
        }

        button.setOnClickListener {
            button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide))

            Handler(Looper.getMainLooper()).postDelayed({
                intent.putExtra(MainActivity.ACTIVITY_ID, buttonInfo.id)
                startActivity(intent)
            }, 1000)

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        button.text = buttonInfo.text
        button.visibility = View.VISIBLE
    }

    private fun setTextView(textView: TextView, text: String) {
        textView.text = if (name == null) text
        else text.format(name)

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

    private fun setCharacter(drawable: String) {
        binding.character.background = getDrawableResource(drawable)
    }

    private fun getDrawableResource(drawable: String): Drawable? {
        val resourceId = resources.getIdentifier(drawable, DRAWABLE, this.packageName)
        return ResourcesCompat.getDrawable(resources, resourceId, null)
    }
}