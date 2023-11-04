package com.example.pr22_individual_gorbatyukdenis

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.pr22_individual_gorbatyukdenis.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var bindingClass: ActivityLoginBinding
    private lateinit var shPref: SharedPreferences
    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        shPref = getPreferences(MODE_PRIVATE)

        username = bindingClass.login
        password = bindingClass.psswd

        shPref = getPreferences(MODE_PRIVATE)

        username.setText(shPref.getString("username", "").toString())
        password.setText(shPref.getString("password", "").toString())

        bindingClass.auth.setOnClickListener {
            if (username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                if(shPref.getString("count", "").toString() == "1") {
                    if (username.text.toString() == shPref.getString("username", "").toString() && password.text.toString() == shPref.getString("password", "").toString()) {
                        val ed3 = shPref.edit()
                        ed3.putString("count", "0")
                        ed3.apply()

                        val ed4 = shPref.edit()
                        ed4.putString("username", username.text.toString())
                        ed4.putString("password", password.text.toString())
                        ed4.apply()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect login or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                        val ed1 = shPref.edit()
                        ed1.putString("count", "1")
                        ed1.apply()

                        val ed2 = shPref.edit()
                        ed2.putString("username", username.text.toString())
                        ed2.putString("password", password.text.toString())
                        ed2.apply()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "пустые поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}