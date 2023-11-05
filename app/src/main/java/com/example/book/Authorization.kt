package com.example.book

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import com.example.book.databinding.ActivityAuthorizationBinding

class Authorization : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var shPref: SharedPreferences
    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shPref = getPreferences(MODE_PRIVATE)


        shPref = getPreferences(MODE_PRIVATE)

        username = binding.editTextTextPersonName
        password = binding.editTextTextPassword

        username.setText(shPref.getString("username", "").toString())
        password.setText(shPref.getString("password", "").toString())

        binding.buttonSignIn.setOnClickListener {

            if (username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {

                if (shPref.getString("count", "").toString() == "1") {

                    if (username.text.toString() == shPref.getString("username", "")
                            .toString() && password.text.toString() == shPref.getString(
                            "password",
                            ""
                        ).toString()
                    ) {
                        val ed = shPref.edit()
                        ed.putString("username", username.text.toString())
                        ed.putString("password", password.text.toString())
                        ed.apply()

                        startActivity(
                            Intent(
                                this@Authorization,
                                SearchBook::class.java
                            )
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "enteredincorrectly",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {

                    val ed1 = shPref.edit()
                    ed1.putString("count", "1")
                    ed1.apply()

                    val ed2 = shPref.edit()
                    ed2.putString("username", username.text.toString())
                    ed2.putString("password", password.text.toString())
                    ed2.apply()

                    startActivity(
                        Intent(
                            this@Authorization,
                            SearchBook::class.java
                        )
                    )
                    finish()
                }
            }
        }
    }
}