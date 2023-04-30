package com.nccf.nccfmobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class InfoFragmentMainPage : Fragment() {

    private lateinit var historyPageButton: CardView
    private lateinit var currentExecutivesButton: CardView
    private lateinit var nccfRulesButton: CardView
    private lateinit var nccfSongButton: CardView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info_main_page, container, false)

        historyPageButton = view.findViewById(R.id.historyPageCardView)
        historyPageButton.setOnClickListener { navigateToPage("history") }
        currentExecutivesButton = view.findViewById(R.id.currentExecutivesPageCardView)
        currentExecutivesButton.setOnClickListener { navigateToPage("current_executives") }
        nccfRulesButton = view.findViewById(R.id.rulesPageCardView)
        nccfRulesButton.setOnClickListener { navigateToPage("rules") }
        nccfSongButton = view.findViewById(R.id.anthemPageCardView)
        nccfSongButton.setOnClickListener { navigateToPage("song") }
        navController = findNavController()

        return view
    }

    private fun navigateToPage(destination: String) {
        when(destination) {
            "history" -> navController.navigate(R.id.action_infoFragmentMainPage_to_historyPageFragment)
            "current_executives" -> navController.navigate(R.id.action_infoFragmentMainPage_to_currentExecutivesPage)
            "rules" -> navController.navigate(R.id.action_infoFragmentMainPage_to_rulesPage)
            "song" -> navController.navigate(R.id.action_infoFragmentMainPage_to_NCCFSongPage)
            else -> Toast.makeText(requireContext(), "Invalid Destination: $destination", Toast.LENGTH_SHORT).show()
        }
    }
}