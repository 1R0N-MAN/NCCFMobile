package com.nccf.nccfmobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPageFragment : Fragment() {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navHostFragmentMainPage: FragmentContainerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)

        navHostFragmentMainPage = view.findViewById(R.id.navHostFragmentMainPage)
        navController = findNavController()
        bottomNavView = view.findViewById(R.id.bottomNavView)
        bottomNavView.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null
            when(item.itemId){
                R.id.homeFragment -> fragment = HomeFragment()
                R.id.paymentFragment -> fragment = PaymentFragment()
                R.id.eventsFragment -> fragment = EventsFragment()
                R.id.infoFragment -> fragment = InfoFragment()
                R.id.settingsFragment -> fragment = SettingsFragment()
            }
            fragment?.let {
                childFragmentManager.beginTransaction().replace(R.id.navHostFragmentMainPage, it).commit()
            }
            true
        }
        return view
    }
}