package com.nccf.nccfmobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val auth = Firebase.auth
        val user = auth.currentUser
        var username = ""

        if (user != null){
            username = user.displayName.toString()
        }

        val welcomeTextView = view.findViewById<TextView>(R.id.welcomeText)
        val welcomeText = getString(R.string.welcome_text, username)

        welcomeTextView.text = welcomeText
        return view
    }
}