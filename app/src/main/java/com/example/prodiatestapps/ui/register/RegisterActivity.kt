package com.example.prodiatestapps.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prodiatestapps.R
import com.example.prodiatestapps.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterView.InitView {
    var name: String? = null
    var email: String? = null
    var pwd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val presenter = RegisterPresenter(this)

        buttonRegister.setOnClickListener {
            name = editTextName.text.toString().trim()
            email = editTextEmail.text.toString().trim()
            pwd = editTextPassword.text.toString().trim()

            if (name?.isEmpty()!!){
                editTextName.error = "nama tidak boleh kosong"
            }else if (!isValidEmail(email)){
                editTextEmail.error = "email tidak valid"
            }else if (pwd?.isEmpty()!!){
                editTextPassword.error = "password tidak boleh kosong"
            }else{
                presenter.register(name, email, pwd)
            }
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

    override fun registerSuccess() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("pwd", pwd)
        startActivity(intent)
    }

    override fun registerFailed(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}