package com.bruno.parsegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (ParseUser.getCurrentUser()!=null){
            gotoMainActivity()

        }else{

        }

        findViewById<Button>(R.id.btn_login).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_userName).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username,password)
        }
        findViewById<Button>(R.id.btn_signup).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_userName).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username,password)
        }
    }



    private fun signUpUser(username: String, password: String){
        val user = ParseUser()
        user.username=username
        user.setPassword(password)

        user.signUpInBackground { e ->
            if(e==null){
                Toast.makeText(this,"Signup successful!",Toast.LENGTH_SHORT).show()
                gotoMainActivity()
            }else{
                Toast.makeText(this,"Signup not successful",Toast.LENGTH_SHORT).show()

                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                gotoMainActivity()
                Log.i(TAG,"Successfully logged in user")
            } else {
                e.printStackTrace()
                Toast.makeText(this,"Error logging in",Toast.LENGTH_SHORT).show()
                // Signup failed.  Look at the ParseException to see what happened.
            }})
        )
    }
    private fun gotoMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    companion object{
        const val TAG = "LoginActivity"
    }
}