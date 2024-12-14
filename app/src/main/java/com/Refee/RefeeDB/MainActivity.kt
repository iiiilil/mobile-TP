package com.Refee.RefeeDB

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val join = findViewById<Button>(R.id.join)
        val login = findViewById<Button>(R.id.login)

        join.setOnClickListener {
            var intent = Intent(applicationContext,Register::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val email = findViewById<EditText>(R.id.email)
            val password = findViewById<EditText>(R.id.password)
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "로그인 완료", Toast.LENGTH_LONG).show()
                        var intent = Intent(applicationContext, MainScreen::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}