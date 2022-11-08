package com.example.socialx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialx.databinding.FragmentLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var _binding : FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var callBackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        callBackManager = CallbackManager.Factory.create()

        auth = Firebase.auth
        Log.d("MyInfo" , "CurrentUser : " + auth.currentUser)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.registerText.setOnClickListener {
            (activity as MainActivity).gotoSignup()
        }

        _binding.googleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }

        _binding.fbLogin.setPermissions(listOf("email"))

        _binding.fbLogin.registerCallback(callBackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                try {
                    handleFacebookAccessToken(result!!.accessToken)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancel() {
                Log.d("MyInfo", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("MyInfo" , "facebook:error  ${error.message}")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful
                val account = task.getResult(ApiException::class.java)!!
                Log.d("MyInfo", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w("MyInfo", "Google sign in failed", e)
            }
        }
        if(requestCode == FB_SIGN_IN){
            callBackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("MyInfo", "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("MyInfo" , "User Verified ${user?.email}")
                    (activity as MainActivity).startIntent()
                } else {
                    // If sign in fails
                    Log.w("MyInfo", "signInWithCredential:failure", task.exception)
                    Log.d("MyInfo" , "User Not Verified")
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("MyInfo", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("MyInfo", "signInWithCredential:success")
                    (activity as MainActivity).startIntent()
                } else {
                    // If sign in fails
                    Log.w("MyInfo", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val GOOGLE_SIGN_IN = 8888
        private const val FB_SIGN_IN = 9999
    }
}