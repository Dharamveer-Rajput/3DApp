package com.a3dapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.BaseAdapter
import android.widget.Toast
import com.a3dapp.R
import com.a3dapp.R.id.imageCameraGrid
import com.a3dapp.models.ImagesModel
import kotlinx.android.synthetic.main.activity_captured_images.*
import kotlinx.android.synthetic.main.grid_item_layout.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.widget.ImageView
import android.R.attr.data
import android.content.res.AssetManager
import android.graphics.Matrix
import android.net.Uri
import android.os.Handler
import android.util.Log
import dmax.dialog.SpotsDialog


/**
 * Created by dharamveer on 17/3/18.
 */
class ImagesGridActivity3 : AppCompatActivity(), View.OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val inverse = Matrix()
          imageViewGl!!.getImageMatrix().invert(inverse);

        val pts = floatArrayOf(event!!.getX(), event.getY())
        inverse.mapPoints(pts);


        return false;
    }


    var adapter: ImagesAdapter? = null
    var imagesList = ArrayList<ImagesModel>()
    val CAMERA_REQUEST_CODE = 0
    lateinit var imageFilePath: String
    var imageViewGl: ImageView? = null
    private val TAKE_PICTURE_REQUEST_B = 100
    private var mCameraBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_captured_images)


        txtCreate.setOnClickListener{

            val dialog = SpotsDialog(this,R.style.CustomProgressDialog)
            dialog.show()


            val mHandler = Handler()
            mHandler.postDelayed(Runnable {
                //start your activity here
                val intent = Intent(this, ImageViewerActivity4::class.java)
                startActivity(intent)

            }, 1000L)



        }


        val assets = applicationContext.assets
        var models: Array<String>? = null
        try {
            models = assets.list("models")
        } catch (ex: IOException) {
            Toast.makeText(applicationContext, ex.message, Toast.LENGTH_LONG).show()
            return
        }


        // load items


        imagesList.add(ImagesModel("Front View", R.drawable.imagesam))
        imagesList.add(ImagesModel("Back View", R.drawable.person))
        imagesList.add(ImagesModel("Left View ", R.drawable.person))
        imagesList.add(ImagesModel("Right View",R.drawable.imagesam))

        adapter = ImagesAdapter(this, imagesList);
        adapter!!.setOnItemClickListener(object : ImagesAdapter.CameraClickListener {
            override fun onCameraClick(position: Int,imageView: ImageView) {

               // Toast.makeText(this@ImagesGridActivity3,position.toString(),Toast.LENGTH_LONG).show()

                 imageViewGl  = imageView

                startImageCapture()


                /*  try {
                    val imageFile = createImageFile()
                    val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if(callCameraIntent.resolveActivity(packageManager) != null) {
                        val authorities = packageName + ".fileprovider"
                        val imageUri = FileProvider.getUriForFile(this@ImagesGridActivity3, authorities, imageFile)
                        callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@ImagesGridActivity3, "Could not create file!", Toast.LENGTH_SHORT).show()
                }*/

            }




        })


        gridCapture.adapter = adapter




    }


    private fun startImageCapture() {
        // startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE_REQUEST_B);

        //camera stuff
        val imageIntent = Intent(this@ImagesGridActivity3,CameraActivity::class.java)
      //  val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())


        //folder stuff
        val imagesFolder = File(Environment.getExternalStorageDirectory(), "AA Inc.")
        imagesFolder.mkdirs()

        val image = File(imagesFolder, "QR_$timeStamp.jpeg")
        val uriSavedImage = Uri.fromFile(image)

        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
      //  startActivityForResult(imageIntent, TAKE_PICTURE_REQUEST_B)


        startActivityForResult(imageIntent, TAKE_PICTURE_REQUEST_B)
    }




    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "-"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!storageDir.exists()) storageDir.mkdir()
        val imageFile = File.createTempFile(imageFileName,".jpg",storageDir)
        imageFilePath  = imageFile.absolutePath
        return imageFile

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == TAKE_PICTURE_REQUEST_B) {
            if (resultCode == Activity.RESULT_OK) {
                // Recycle the previous bitmap.

                if (mCameraBitmap != null && !mCameraBitmap!!.isRecycled()) {
                   // mCameraBitmap!!.recycle();
                    mCameraBitmap = null;
                }


                val extras = data.extras
                //   mCameraBitmap = (Bitmap) extras.get("data");
                val cameraData = extras!!.getByteArray(CameraActivity.EXTRA_CAMERA_DATA)
                if (cameraData != null) {
                    mCameraBitmap = BitmapFactory.decodeByteArray(cameraData, 0, cameraData.size)


                    imageViewGl!!.setImageBitmap(RotateBitmap(mCameraBitmap!!,90f))


                    imageViewGl!!.setOnTouchListener(this)

                   // mSaveImageButton.setEnabled(true)
                }
            } else {
                mCameraBitmap = null
               // mSaveImageButton.setEnabled(false)
            }
        }


    }

    fun RotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                     *//*           if(resultCode == Activity.RESULT_OK && data != null) {
                     photoImageView.setImageBitmap(data.extras.get("data") as Bitmap)
                 }*//*

                if(resultCode == Activity.RESULT_OK) {


                        imageViewGl?.setImageBitmap(setScaledBitmap())


                    Toast.makeText(this, "Photo has been captured", Toast.LENGTH_SHORT).show()

                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }


        }
    }*/



      fun setScaledBitmap(): Bitmap {
          val imageViewWidth = imageViewGl?.width
          val imageViewHeight = imageViewGl?.height

          val bmOptions = BitmapFactory.Options()
          bmOptions.inJustDecodeBounds = true
          BitmapFactory.decodeFile(imageFilePath, bmOptions)
          val bitmapWidth = bmOptions.outWidth
          val bitmapHeight = bmOptions.outHeight

          val scaleFactor = Math.min(bitmapWidth/ imageViewWidth!!, bitmapHeight/ imageViewHeight!!)

          bmOptions.inJustDecodeBounds = false
          bmOptions.inSampleSize = scaleFactor

          return BitmapFactory.decodeFile(imageFilePath, bmOptions)

      }


    class ImagesAdapter : BaseAdapter {
        var foodsList = ArrayList<ImagesModel>()
        var context: Context? = null

        var cameraClickListener: CameraClickListener? = null


        constructor(context: Context, foodsList: ArrayList<ImagesModel>) : super() {
            this.context = context
            this.foodsList = foodsList
        }

        interface CameraClickListener {

            fun onCameraClick(position: Int,imageView: ImageView)

        }

        fun setOnItemClickListener(cameraClickListener: CameraClickListener) {
            this.cameraClickListener = cameraClickListener

        }

        override fun getCount(): Int {
            return foodsList.size
        }

        override fun getItem(position: Int): Any {
            return foodsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val food = this.foodsList[position]
            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var gridView = inflator.inflate(R.layout.grid_item_layout, null)
            gridView.imagePerson.setImageResource(food.image!!)

           /* try {
                val bitmap = BitmapFactory.decodeStream(context!!.getAssets().open(foodsList[position].image))
                gridView.imagePerson.setImageBitmap(bitmap)
            } catch (e: Exception) {
                gridView.imagePerson.setImageResource(R.drawable.person)
            }*/



           // gridView.imagePerson.setImageResource(food.image!!)
            gridView.tvName.text = food.name!!

            gridView.imageCameraGrid.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {

                    cameraClickListener?.onCameraClick(position,gridView.imagePerson)
                }


            })


            return gridView
        }







    }



}
