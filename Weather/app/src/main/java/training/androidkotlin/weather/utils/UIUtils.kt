package training.androidkotlin.weather.utils

import android.content.Context
import android.widget.Toast
import java.time.Duration

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}