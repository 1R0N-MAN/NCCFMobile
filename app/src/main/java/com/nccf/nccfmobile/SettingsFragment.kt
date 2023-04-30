package com.nccf.nccfmobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val logoutButton = view.findViewById<CardView>(R.id.logoutButton)
        logoutButton.setOnClickListener { logout() }

        auth = Firebase.auth
        navController = findNavController()
        return view
    }

    private fun logout() {
        auth.signOut()
        removeLoggedInStatusFromSharedPreferences()
        Toast.makeText(requireContext(), "Logging out user...", Toast.LENGTH_SHORT).show()
        openLoginFragment()
    }

    private fun openLoginFragment() {
        val mainPageFragment = parentFragment as MainPageFragment
        val mainPageNavController = mainPageFragment.findNavController()
        mainPageNavController.navigate(R.id.action_mainPageFragment_to_loginFragment)
    }

    private fun removeLoggedInStatusFromSharedPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEEP_ME_LOGGED_IN, false)
        editor.apply()
    }
}