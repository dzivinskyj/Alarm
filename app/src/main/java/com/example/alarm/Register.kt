package com.example.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.parse.ParseUser
import java.util.regex.Pattern

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view : View){
        //Toast.makeText(this, "Let's try", Toast.LENGTH_SHORT).show();
        var login = findViewById<EditText>(R.id.login)
        var email = findViewById<EditText>(R.id.email)
        var pass = findViewById<EditText>(R.id.password)
        var pass2 = findViewById<EditText>(R.id.password2)
        var error = false

        if(login.text.toString().length < 5){
            login.error = "Minimun 5 znaków!"
            error = true
        }
        if(isEmailValid(email.text.toString())){
            email.error = "Zły format maila"
            error = true
        }
        if(pass.text.toString().length < 5){
            pass.error = "Minimun 5 znaków!"
            error = true
        }
        if(!pass2.text.toString().equals(pass.text.toString())){
            pass2.error = "Hasła muszą się zgadzać"
            error = true
        }

        if(!error) {
            val user = ParseUser()
            user.username = login.text.toString()
            user.email = email.text.toString()
            user.setPassword(pass.text.toString())
            user.signUpInBackground { e ->
                if (e == null) {
                    alertDisplayer("Udało się zarejestrować!", "Witaj " + login.text.toString() + "!")
                } else {
                    ParseUser.logOut()
                    Toast.makeText(this@Register, "Taki użytkownik już istnieje", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    private fun alertDisplayer(title:String, message:String) {
        val builder = AlertDialog.Builder(this@Register)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
                // don't forget to change the line below with the names of your Activities
                val intent = Intent(this@Register, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        val ok = builder.create()
        ok.show()
    }
}
