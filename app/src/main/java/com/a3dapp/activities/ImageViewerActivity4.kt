package com.a3dapp.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.a3dapp.R
import com.a3dapp.util.GLView
import kotlinx.android.synthetic.main.image_viewer_layout.*

/**
 * Created by dharamveer on 17/3/18.
 */

class ImageViewerActivity4: AppCompatActivity() {


    internal lateinit var view: GLView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.image_viewer_layout)



    }

}