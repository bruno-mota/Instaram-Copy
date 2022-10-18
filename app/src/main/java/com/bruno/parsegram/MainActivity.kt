package com.bruno.parsegram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar

import androidx.core.content.FileProvider
import com.bruno.parsegram.Modules.Post
import com.parse.Parse
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File


class MainActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var ivPreview: ImageView
    lateinit var ivPlaceholder:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivPreview = findViewById(R.id.pictureFromCamera)
        ivPlaceholder = findViewById(R.id.camera_placeholder)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        findViewById<Button>(R.id.btn_takePicture).setOnClickListener{
            onLaunchCamera()
        }
        findViewById<Button>(R.id.btn_submit ).setOnClickListener{
            val description = findViewById<EditText>(R.id.et_description).text.toString()
            val user=ParseUser.getCurrentUser()
            if(photoFile!=null){
                submitPost(description,user,photoFile!!)
            }else{
                Toast.makeText(this, "Please take a picture", Toast.LENGTH_SHORT).show()

            }

        }
        /*var cameraResultLauncher: ActivityResultLauncher<Intent>? = null
        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview:ImageView = findViewById(R.id.pictureFromCamera)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }*/

        //queryForPosts()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        ParseUser.logOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
        Log.i(TAG,"LOGGED OUT")
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode== RESULT_OK){
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)

                ivPlaceholder.visibility = View.GONE
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(takenImage)
                ivPreview.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }else{
                Toast.makeText(this, "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitPost(description:String, user:ParseUser,file:File){
        val post= Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        val progressBar: ProgressBar = findViewById(R.id.pbLoading)
        progressBar.visibility = View.VISIBLE
        post.saveInBackground { exception->

            if(exception!=null){
                Log.e(TAG,"Error while saving post")
                Toast.makeText(this, "Post upload failed.", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                exception.printStackTrace()
            }else{
                Log.i(TAG,"Successfully saved post")
                Toast.makeText(this, "Successfully uploaded the post!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE

                ivPlaceholder.visibility = View.VISIBLE
                ivPreview.visibility = View.INVISIBLE
            }
        }
    }
    private fun queryForPosts() {
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching posts")
            } else {
                if (posts != null) {
                    for (post in posts) {
                        Log.i(TAG, "Post:  " + post.getDescription()+ " ,username: "+
                                post.getUser()?.username)
                    }
                }
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
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
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
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    companion object{
        const val TAG = "MainActivity"
    }

}