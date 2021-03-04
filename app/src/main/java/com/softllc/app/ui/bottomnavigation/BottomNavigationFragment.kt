package com.softllc.app.ui.bottomnavigation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softllc.app.DashboardApplication
import com.softllc.app.R
import com.softllc.app.R.layout.fragment_bottom_navigation
import com.softllc.app.databinding.ActivityMainBinding
import com.softllc.app.databinding.FragmentBottomNavigationBinding
import timber.log.Timber

class BottomNavigationFragment : Fragment () {

    companion object {
        fun newInstance() = BottomNavigationFragment()
    }

    private lateinit var binding : FragmentBottomNavigationBinding
    private lateinit var viewModel: BottomNavigationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBottomNavigationBinding.inflate(this.layoutInflater ,container, false)

        Timber.d("onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView: BottomNavigationView = binding.navView
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_bottom_fragment)
        navView.setupWithNavController(navController)


        controller = navController

        binding.navView.apply {
        }

        //todo
        // use dashboard plugin to create
        // 1. NavGraph to replace bottom_navigation_root
        // 2. Menu for BottomNavigationView
        // 3. set of topLevelDestinationId for AppBarConfiguration
        //

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_root, R.id.freeze_fragment_root, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController((requireActivity() as AppCompatActivity), navController, appBarConfiguration)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BottomNavigationViewModel::class.java)
        // TODO: Use the ViewModel

    }

    private lateinit var controller: NavController // don't forget to initialize

    @SuppressLint("RestrictedApi")
    private val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        // react on change
        Timber.d("OnDestinationChangedListener$destination")
        val backstack = controller.currentBackStackEntry
        Timber.d("${backstack?.destination}")
        Timber.d("djm ${backstack?.destination?.navigatorName}")
        val fragmentName = backstack?.destination?.displayName
        val navName = backstack?.destination?.navigatorName
        if ( fragmentName == "com.softllc.generic.blue:id/fragment_home" ||
                fragmentName == "com.softllc.generic.blue:id/freeze_fragment" ||
                fragmentName == "com.softllc.generic.blue:id/navigation_notifications") {
            binding.navView.visibility = View.VISIBLE
        }
        else {
            binding.navView.visibility = View.GONE
        }



       // if ( destination.id == binding.navView.visibility = View.GONE
        // you can check destination.id or destination.label and act based on that
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        controller.removeOnDestinationChangedListener(listener)
        super.onPause()
    }

}