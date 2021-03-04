package com.softllc.app.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.softllc.auth.api.AuthService
import com.softllc.auth.api.BusinessDb
import com.softllc.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel  by viewModels()

    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragmentHomeBinding = FragmentHomeBinding.inflate(this.layoutInflater, container, false)
        fragmentHomeBinding.viewModel = homeViewModel
        fragmentHomeBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentHomeBinding.root
    }

    val RC_SIGN_IN = 101
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter<BusinessDb>(requireContext(), android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fragmentHomeBinding.businesses.adapter = adapter

        homeViewModel.businesses.observe(viewLifecycleOwner) {
            adapter.clear()
            adapter.addAll(it)
        }

        homeViewModel.currentUser.observe(viewLifecycleOwner) {
            setLoginButtonText()
        }
        homeViewModel.signInBusy.observe(viewLifecycleOwner) {
            fragmentHomeBinding.login.isEnabled = !it
        }

        fragmentHomeBinding.login.setOnClickListener {
            doLogin()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        
    }

    val onBackPressedCallback = object : OnBackPressedCallback(
        true
    ) {
        override fun handleOnBackPressed() {
            showBackDialog(this)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        _fragmentHomeBinding = null
    }
     fun setLoginButtonText()
    {
        val user = homeViewModel.currentUser.value

        fragmentHomeBinding.login.text =
                if ( user == null || user.id.isNullOrBlank() ) {
                    "Login"
                }
                else {
                    if (  user.anonymous ) {
                        "Guest"
                    }
                    else {
                        user.name
                    }
                }
    }
    fun showBackDialog(onBackPressedCallback: OnBackPressedCallback) {
        // setup the alert builder
        val builder =  AlertDialog.Builder(requireContext())
        builder.setTitle("Exit Dashboard")
        builder.setMessage("Would you like to exit?")
        // add the buttons
        builder.setPositiveButton("Continue", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                onBackPressedCallback.remove()
                requireActivity().onBackPressed()
            }

        })
        builder.setNegativeButton("Cancel", null)
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }



    fun doLogin() {
        fragmentHomeBinding.login.isEnabled = false

        val user = homeViewModel.currentUser.value

        if ( user != null && !user.id.isBlank() ) {
            homeViewModel.authService.signOut(requireActivity())
        }
        else {
            homeViewModel.authService.signIn(requireActivity())
        }
    }
}
