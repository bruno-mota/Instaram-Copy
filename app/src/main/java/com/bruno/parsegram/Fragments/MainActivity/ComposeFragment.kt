package com.bruno.parsegram.Fragments.MainActivity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bruno.parsegram.MainActivity
import com.bruno.parsegram.Modules.Post
import com.bruno.parsegram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class ComposeFragment : Fragment() {
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    lateinit var ivPreview: ImageView
    lateinit var ivPlaceholder: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivPreview = view.findViewById(R.id.pictureFromCamera)
        ivPlaceholder = view.findViewById(R.id.camera_placeholder)
        view.findViewById<Button>(R.id.btn_takePicture).setOnClickListener{
            onLaunchCamera()
        }
        view.findViewById<Button>(R.id.btn_submit ).setOnClickListener{
            val description = view.findViewById<EditText>(R.id.et_description).text.toString()
            val user= ParseUser.getCurrentUser()
            if(photoFile!=null){
                submitPost(description,user,photoFile!!)
            }else{
                Toast.makeText(requireContext(), "Please take a picture", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode== AppCompatActivity.RESULT_OK){
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)

                ivPlaceholder.visibility = View.GONE
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(takenImage)
                ivPreview.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }else{
                Toast.makeText(requireContext(), "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun submitPost(description:String, user:ParseUser,file: File){
        val post= Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        val progressBar: ProgressBar = requireView().findViewById(R.id.pbLoading)
        progressBar.visibility = View.VISIBLE
        post.saveInBackground { exception->
            if(exception!=null){
                Log.e(MainActivity.TAG,"Error while saving post")
                Toast.makeText(requireContext(), "Post upload failed.", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                exception.printStackTrace()
            }else{
                Log.i(MainActivity.TAG,"Successfully saved post")
                Toast.makeText(requireContext(), "Successfully uploaded the post!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE

                ivPlaceholder.visibility = View.VISIBLE
                ivPreview.visibility = View.INVISIBLE
            }
        }
    }
    private fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }
        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
}