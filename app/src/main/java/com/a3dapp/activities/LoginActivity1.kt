package com.a3dapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.a3dapp.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login_main.*

class LoginActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContentView(R.layout.activity_login_main)

        edPassword.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == R.id.login || i == EditorInfo.IME_NULL) {
                login()
                return@setOnEditorActionListener true
            }
            false
        }

        btnSignIn.setOnClickListener {
            login()
        }



    }

    private fun login() {

        if(TextUtils.isEmpty(edEmail.text.toString())){
            edEmail.setError("enter email")
        } else if(TextUtils.isEmpty(edPassword.text.toString())){
            edPassword.setError("enter password")
        }
        else{

            val dialog = SpotsDialog(this,R.style.CustomProgressDialog)
            dialog.show()

            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)



        }


    }


}
