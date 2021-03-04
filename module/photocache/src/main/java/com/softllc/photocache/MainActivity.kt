
package com.softllc.photocache


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Parcelable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.setupWithNavController
import com.softllc.photocache.Analytic.LogAnalyticEvent
import com.softllc.photocache.databinding.ActivityMainBinding
import com.softllc.photocache.utilities.FileUtils
import com.softllc.photocache.utilities.runOnIoThread
import java.util.*


class MainActivity : AppCompatActivity() {

    //private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    //private var photoListViewModel: MainViewModel  by viewModels()
    private val photoListViewModel: PhotoViewModel  by viewModels()

    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            val photoId = UUID.randomUUID().toString()
            runOnIoThread {
               // val fileName = FileUtils(this).upload(userId, photoId, it.toString())
              //  photoListViewModel.addPhoto(photoId, fileName)
                LogAnalyticEvent(Analytic.Event.ADD_PHOTO, "copy_single")
            }
        }
    }

    private fun handleSendMultipleImages(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            runOnIoThread {
                for (i in it.reversed()) {
                    val photoId = UUID.randomUUID().toString()

                   // val fileName = FileUtils(this).upload(photoId, i.toString())
                 //   photoListViewModel.addPhoto(photoId, fileName)
                    LogAnalyticEvent(Analytic.Event.ADD_PHOTO, "copy_multiple")
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
         } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }


        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        navController = Navigation.findNavController(this, R.id.freeze_nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up navigation menu
        binding.navigationView.setupWithNavController(navController)

        FreezeApp.locked.observe(this, Observer { locked ->
            when (locked) {
                true -> binding.appbar.visibility = GONE
                false -> binding.appbar.visibility = VISIBLE
            }
        })


        processIntent(intent)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        setSupportActionBar(null)

    }

    private fun processIntent(intent: Intent?) {
        if (intent != null) {
            when {
                intent.action == Intent.ACTION_SEND -> {
                    if (intent.type?.startsWith("image/") == true) {
                        handleSendImage(intent) // Handle single image being sent
                    }
                }
                intent.action == Intent.ACTION_SEND_MULTIPLE -> {
                    handleSendMultipleImages(intent) // Handle multiple images being sent
                }
                else -> {
                    // Handle other intents, such as being started from the home screen
                }
            }
            intent.replaceExtras(Bundle());
            intent.setAction("");
            intent.setData(null);
            intent.setFlags(0);
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}

