package com.nccf.nccfmobile

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: TextView
    private lateinit var forgotPasswordButton: TextView
    private lateinit var navController: NavController
    private lateinit var dialog: AlertDialog
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var keepMeLoggedInCheckBox: CheckBox


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        loginButton.setOnClickListener { initiateLogin() }
        signupButton = view.findViewById(R.id.signUpTextView)
        signupButton.setOnClickListener { openSignupPage() }
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordTextView)
        forgotPasswordButton.setOnClickListener { openForgotPasswordPage() }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        keepMeLoggedInCheckBox = view.findViewById(R.id.keepMeLoggedIn)
        navController = findNavController()
        auth = Firebase.auth

        checkIfUserIsLoggedIn()
        return view
    }

    private fun checkIfUserIsLoggedIn() {
        if (auth.currentUser != null){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val isLoggedIn = sharedPreferences.getBoolean(KEEP_ME_LOGGED_IN, false)

            if (isLoggedIn){
                // Open NigMart Store
                val action = R.id.action_loginFragment_to_mainPageFragment
                navController.navigate(action)
            }
            else {
                Log.d(tag, "User not logged in! Opening Login Page...")
            }
        }
    }

    private fun openForgotPasswordPage() {
        navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
    }

    private fun openSignupPage() {
        navController.navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    private fun initiateLogin(){
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        when {
            email.isEmpty() -> {
                Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
            }
            password.length < 8 -> {
                Toast.makeText(context, "Password must contain at least 8 characters", Toast.LENGTH_SHORT).show()
            }
            !containsAlphabetAndDigit(password) -> {
                Toast.makeText(context, "Password must contain at least one alphabet and one digit", Toast.LENGTH_SHORT).show()
            }
            else -> {
                loginUser(email, password)
            }
        }
    }

    private fun containsAlphabetAndDigit(input: String): Boolean {
        val regex = "^(?=.*[A-Za-z])(?=.*\\d).+\$".toRegex()
        return regex.matches(input)
    }

    private fun loginUser(email: String, password: String) {
        showLoadingPopup()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                val editor = sharedPreferences.edit()
                if (user != null){
                    // check if email is verified
                    if (user.isEmailVerified){
                        dialog.dismiss()
                        // Store the Keep Me Logged in state so users don't have to login later
                        val keepMeLoggedIn = keepMeLoggedInCheckBox.isChecked
                        editor.putBoolean(KEEP_ME_LOGGED_IN, keepMeLoggedIn)
                        editor.apply()

                        openMainPage()
                    } else {
                        dialog.dismiss()
                        // send verification email if email is not verified
                        openVerificationDialog(email)
                    }
                }
            }
        }.addOnFailureListener { exception ->
            dialog.dismiss()
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMainPage() {
        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
        // Navigate to the Main Page
        navController.navigate(R.id.action_loginFragment_to_mainPageFragment)
    }

    private fun openVerificationDialog(email: String){
        // Send verification message to mail
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    // Open verification dialog fragment
                    val dialogFragment = VerificationDialogFragment(email, true)
                    dialogFragment.show(childFragmentManager, "verification_dialog")
                }
                else {
                    val dialogFragment = VerificationDialogFragment(email, false)
                    dialogFragment.show(childFragmentManager, "verification_dialog")
                }
            }
    }

    private fun showLoadingPopup() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_popup, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialog = dialogBuilder.create()
        dialog.show()
    }
}