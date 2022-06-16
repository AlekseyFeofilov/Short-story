package com.example.shortstory

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.shortstory.activityStructure.ActivityStructure
import com.example.shortstory.activityStructure.ButtonColor
import com.example.shortstory.activityStructure.ButtonStructure
import com.example.shortstory.activityStructure.Interface
import com.example.shortstory.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

internal class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    internal enum class StringResources(val string: String) {
        ACTIVITY_ID("activityId"),
        NAME("name"),
        DRAWABLE("drawable"),
        JSON_PATH("appearanceInfo.json"),
    }

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityId = intent.getIntExtra(StringResources.ACTIVITY_ID.string, 0)
        name = intent.getStringExtra(StringResources.NAME.string)

        val json = JSONObject(loadJSONFromAsset(assets)!!)
        val jsonActivityStructure = json.getJSONObject(activityId.toString())
        val activity =
            Gson().fromJson(jsonActivityStructure.toString(), ActivityStructure::class.java)
        interfaceInit(activity)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun interfaceInit(activity: ActivityStructure) {
        binding.backgroundImageView.background = getDrawableResource(activity.backgroundName)
        val intent = Intent(this, MainActivity::class.java)

        for (component in activity._interface) {
            when (component) {
                Interface.TITLE -> setTextView(binding.titleTextView, activity.title!!)
                Interface.QUESTION -> setTextView(
                    binding.questionTextView,
                    activity.question!!
                )
                Interface.SET_NAME -> setSetNameEditText(intent)
                Interface.CHARACTER -> setCharacter(activity.character!!)
                else -> {
                    when (component) {
                        Interface.BUTTON_OPTION1 -> {
                            setButtonOption(
                                binding.buttonOption1,
                                activity.buttonOption1!!,
                                activity.buttonsColor,
                                intent
                            )
                        }
                        Interface.BUTTON_OPTION2 -> {
                            setButtonOption(
                                binding.buttonOption2,
                                activity.buttonOption2!!,
                                activity.buttonsColor,
                                intent
                            )
                        }
                        else -> setButtonOption(
                            binding.buttonOption3,
                            activity.buttonOption3!!,
                            activity.buttonsColor,
                            intent
                        )
                    }
                }
            }
        }
    }

    private fun setButtonOption(
        button: Button,
        buttonInfo: ButtonStructure,
        color: ButtonColor,
        intent: Intent
    ) {
        button.setBackgroundColor(
            ContextCompat.getColor(
                this, if (color == ButtonColor.DARK) R.color.dark else R.color.light
            )
        )

        button.setOnClickListener {
            lifecycleScope.launch {
                button.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.hide))
                button.visibility = View.INVISIBLE
                delay(1000)
                button.visibility = View.VISIBLE
            }

            lifecycleScope.launch{
                delay(300)
                intent.putExtra(StringResources.ACTIVITY_ID.string, buttonInfo.id)
                startActivity(intent)
            }

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        button.text = buttonInfo.text
        button.visibility = View.VISIBLE
    }

    private fun setTextView(textView: TextView, text: String) {
        textView.text = if (name == null) text else text.format(name)
        textView.visibility = View.VISIBLE
    }

    private fun setSetNameEditText(intent: Intent) {
        binding.buttonOption3.isEnabled = false
        binding.setNameEditText.visibility = View.VISIBLE

        binding.setNameEditText.doAfterTextChanged {
            intent.putExtra(StringResources.NAME.string, it.toString())
            binding.buttonOption3.isEnabled = true
        }
    }

    private fun setCharacter(drawable: String) {
        binding.character.background = getDrawableResource(drawable)
    }

    private fun getDrawableResource(drawable: String): Drawable? {
        val resourceId =
            resources.getIdentifier(drawable, StringResources.DRAWABLE.string, this.packageName)
        return ResourcesCompat.getDrawable(resources, resourceId, null)
    }
}