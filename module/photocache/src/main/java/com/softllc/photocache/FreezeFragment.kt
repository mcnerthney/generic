package com.softllc.photocache

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileObserver.DELETE
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.softllc.photocache.Analytic.LogAnalyticEvent
import com.softllc.photocache.databinding.FragmentFreezeBinding
import com.softllc.photocache.usecase.AddPhotoAuthUserNotFound
import com.softllc.photocache.usecase.AddPhotoUseCase
import com.softllc.photocache.utilities.FileUtils
import com.softllc.photocache.utilities.runOnIoThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

@AndroidEntryPoint
class FreezeFragment : Fragment() {

    val photoListViewModel: FreezeViewModel by viewModels()
    val REQUEST_GET_SINGLE_IMAGE = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity = requireActivity()

        val binding = FragmentFreezeBinding.inflate(inflater, container, false)
        val adapter = PhotoAdapter()
        binding.freezeList.adapter = adapter

        photoListViewModel.photos.observe(viewLifecycleOwner, Observer { photos ->
            if (photos != null) {
                binding.hasPhotos = photos.isNotEmpty()
                adapter.submitList(photos)
            } else {
                binding.hasPhotos = false
            }
        })

        photoListViewModel.failure.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {failure ->
                if ( failure is AddPhotoAuthUserNotFound) {
//                    val signInBusy = photoListViewModel.authService.signIn(requireActivity())
//                    signInBusy.asLiveData().observe(viewLifecycleOwner) {
//                        if ( it == false ) {
//                            val user = photoListViewModel.authService.user
//                            if ( !user.id.isNullOrBlank()) {
//                                photoListViewModel.addPhoto(requireActivity(),failure.param.imageSrc)
//                            }
//                        }
//                    }
                    Toast.makeText(requireContext(), "Login to add photo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        photoListViewModel.photo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                // navigate to photo fragment
                val direction = FreezeFragmentDirections.actionFreezeFragmentToPhotoFragment(it.photoId)
                findNavController().navigate(direction)

            }
        }

        FreezeApp.locked.observe(viewLifecycleOwner, Observer { locked ->
            binding.keyguard = locked
        })



        binding.freezeList.layoutManager = GridLayoutManager(activity, 2)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_freeze, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                photoListViewModel.deleteAll()
                true

            }
            R.id.action_add -> {
                launchGallery()
                true

            }
             else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        //intent.putExtra("crop", true);
        //intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {


        if (requestCode == REQUEST_GET_SINGLE_IMAGE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                if (intent != null) {
                    val imageSrc = intent.data?.toString()
                    val addResult = photoListViewModel.addPhoto(imageSrc ?: "")

                }
            }
            super.onActivityResult(requestCode, resultCode, intent)
        }

    }


//    fun copyToFirebaseStorage(filename: String) {
//
//
//        val firebaseStorage = FirebaseStorage.getInstance()
//        val storageRef = firebaseStorage.reference
//
//        // Points to "images"
//        val imagesRef = storageRef.child("images")
//        val file = Uri.fromFile(File(filename))
//        val riversRef = storageRef.child("images/${userId}/${file.lastPathSegment}")
//        val uploadTask = riversRef.putFile(file)
//
//// Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener {
//            // Handle unsuccessful uploads
//        }.addOnSuccessListener { taskSnapshot ->
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//        }
//
//
//    }

}