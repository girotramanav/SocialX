package com.example.socialx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.socialx.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        //auth.signOut()
        if(auth.currentUser!=null){
            startIntent()
        }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.signupTab.setOnClickListener {
            gotoSignup()
        }
        _binding.loginTab.setOnClickListener {
            gotoLogin()
        }
    }

    override fun onStart() {
        super.onStart()
        navController = Navigation.findNavController(_binding.navHostFragment)
    }

    fun gotoSignup(){
        Log.d("MyInfo" , "Signup clicked")
        _binding.signupTab.setBackgroundResource(R.drawable.tab_background)
        _binding.loginTab.setBackgroundResource(R.drawable.white_tab_background)
        _binding.signupTab.setTextColor(ContextCompat.getColor(this , R.color.white))
        _binding.loginTab.setTextColor(ContextCompat.getColor(this , R.color.grey))
        navController.navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    fun gotoLogin(){
        Log.d("MyInfo" , "Login clicked")
        _binding.loginTab.setBackgroundResource(R.drawable.tab_background)
        _binding.signupTab.setBackgroundResource(R.drawable.white_tab_background)
        _binding.loginTab.setTextColor(ContextCompat.getColor(this , R.color.white))
        _binding.signupTab.setTextColor(ContextCompat.getColor(this , R.color.grey))
        navController.navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    fun startIntent(){
        val intent = Intent(this , HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}