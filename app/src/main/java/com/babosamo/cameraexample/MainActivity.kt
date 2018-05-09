package com.babosamo.cameraexample

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth



class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    val TAKE_PHOTO_RESULT_CODE = 100
    val TAKE_PHOTO_FULL_SIZE_RESULT_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Toast.makeText(this, "take a thumbnail potho", Toast.LENGTH_SHORT).show()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, TAKE_PHOTO_RESULT_CODE)
            }
        }


        fab2.setOnClickListener { view ->
            Toast.makeText(this, "take a full size photo", Toast.LENGTH_SHORT).show()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            var photoFile: File? = null
            try {
                photoFile = createImageFile()

                val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, TAKE_PHOTO_FULL_SIZE_RESULT_CODE)

            } catch (ex: IOException) {
                Log.e(TAG, "error: $ex")
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == TAKE_PHOTO_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            var extra = data?.extras

            extra?.let {
                if (it.containsKey("data")) { // buffer 로만 넘겨줌
                    val bitmap: Bitmap = it.get("data") as Bitmap
                    photoImageView.setImageBitmap(bitmap)
                }
            }

        }else if(requestCode == TAKE_PHOTO_FULL_SIZE_RESULT_CODE && resultCode == Activity.RESULT_OK){
            if(mCurrentPhotoPath != null){

                setPic()
//                val imageUri = Uri.parse(mCurrentPhotoPath)
//
//                MediaScannerConnection.scanFile(this, arrayOf(imageUri.path), null) {
//                    path, uri ->
//                }
//
//                val handler = Handler()
//                val runnable = Runnable {
//                    photoImageView.setImageBitmap(bitmap)
//                }
//                handler.postDelayed(runnable, 500)
            }
        }

    }


    private fun setPic() {
        // Get the dimensions of the View
        val targetW = photoImageView.getWidth()
        val targetH = photoImageView.getHeight()

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        photoImageView.setImageBitmap(bitmap)
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

    var mCurrentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "brunch_" + timeStamp + "_"
//        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//        val image = File.createTempFile(
//                imageFileName, /* prefix */
//                ".jpg", /* suffix */
//                path            /* directory */
//        )
//
//        // Save a file: path for use with ACTION_VIEW intents
//        val mCurrentPhotoPath = "file:" + image.absolutePath
//        return image
//    }
}
