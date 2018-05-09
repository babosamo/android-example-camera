package com.babosamo.cameraexample

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    val TAKE_CAMERA_RESULT_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Toast.makeText(this, "take a picture", Toast.LENGTH_SHORT).show()

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                if (photoFile != null) {

                    val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, TAKE_CAMERA_RESULT_CODE)
                }
            } catch (ex: IOException) {
                Log.e(TAG, "error: $ex")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "brunch_" + timeStamp + "_"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                path            /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }
}
