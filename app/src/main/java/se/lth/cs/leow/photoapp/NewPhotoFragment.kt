package se.lth.cs.leow.photoapp

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK

import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import kotlinx.android.synthetic.main.fragment_new_photo.*
import se.lth.cs.leow.photoapp.db.PointEntity
import java.io.File
import java.io.IOException
import java.util.*


class NewPhotoFragment : Fragment() {
    private lateinit var viewModel:SharedViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_photo, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        submit_button.text = "Take photo"
        submit_button.setOnClickListener {
            getPosition()
            initializeCamera()
        }
        val permissionsDenied = viewModel.permissions.filter { it -> !currentPermissions(it) }
        if(permissionsDenied.isNotEmpty()){
            requestPermissions(permissionsDenied.toTypedArray(),
                REQUEST_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it -> it != PackageManager.PERMISSION_DENIED }) {
                Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show()
                getFragmentManager()?.popBackStackImmediate();

            }
        }
    }

    //////////////////// TAKE PHOTO AND REQUIRE URI /////////////////////////////
    private fun currentPermissions(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val file = File(viewModel.currentPhotoPath)
            val imgUri = Uri.fromFile(file)
            viewModel.setLastUriValue(viewModel.lastUri)
            camera_view.setImageURI(imgUri)
            submit_button.text = "Submit"
            position_text.text = "${viewModel.lastLatitude} °N, ${viewModel.lastLongitude} °E "
            submit_button.setOnClickListener {
                viewModel.addPoint(PointEntity(0,viewModel.lastLatitude!!,viewModel.lastLongitude!!,imgUri.toString()))
                getFragmentManager()?.popBackStackImmediate();
            }
        }
        else{
            getFragmentManager()?.popBackStackImmediate();
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir as File/* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            viewModel.currentPhotoPath = absolutePath
        }
    }

    private fun initializeCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "se.lth.cs.leow.android.fileprovider",
                it
            )
            startCamera(photoUri)
        }
    }

    private fun startCamera(photoUri: Uri) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(requireContext().packageManager) != null){
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

    }

    //Called from method which check permission
    @SuppressLint("MissingPermission")
    private fun getPosition(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location != null){
                    viewModel.setLastCoordinates(location.latitude, location.longitude)
                }
            }
    }


    companion object{
        private const val REQUEST_PERMISSION = 10
        private const val REQUEST_IMAGE_CAPTURE = 2
    }
    }