package com.nymbleup.inventory.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nymbleup.inventory.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SignInActivity::class.java))
        }

    }

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}