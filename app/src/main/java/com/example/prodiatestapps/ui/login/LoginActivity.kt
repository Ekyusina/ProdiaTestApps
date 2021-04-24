package com.example.prodiatestapps.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prodiatestapps.MainActivity
import com.example.prodiatestapps.R
import com.example.prodiatestapps.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView.InitView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val presenter = LoginPresenter(this)

        val bundle = intent.extras
        var email = bundle?.getString("email")
        var pwd = bundle?.getString("pwd")

        editTextEmail.setText(email)
        editTextPassword.setText(pwd)

        buttonLogin.setOnClickListener {
            email = editTextEmail.text.toString().trim()
            pwd = editTextPassword.text.toString().trim()

            if (!isValidEmail(email)){
                editTextEmail.error = "email tidak valid"
            }else if (pwd?.isEmpty()!!){
                editTextPassword.error = "Password tidak boleh kosong"
            }else{
                presenter.login(email, pwd)
            }
        }

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun isValidEmail(email: CharSequence?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun loginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun loginFailed(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}