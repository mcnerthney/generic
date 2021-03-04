package com.softllc.app

import android.os.Bundle
import androidx.appcompat.app.ActionBar.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.softllc.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("onCreateView")

        supportActionBar?.apply {
            setDisplayOptions(DISPLAY_HOME_AS_UP + DISPLAY_SHOW_HOME + DISPLAY_SHOW_TITLE)
            //setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
            setHomeButtonEnabled(true)
            //hide()
        }
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main)

        val dashboardApplication = application
        // if ( dashboardApplication is DashboardApplication ) {

        //    dashboardApplication.initialized.observe(this) {
        //        Timber.d("initialized $it")
        //        if ( it == true ) {

        //           dashboardApplication.initialized.removeObservers(this)

    }
}