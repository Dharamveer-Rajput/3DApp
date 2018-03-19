package com.a3dapp.models

/**
 * Created by dharamveer on 15/3/18.
 */
class ImagesModel {

    var name: String? = null
    var image: String? = null

    constructor(path: String,name: String, image: String) {
        this.name = name
        this.image = image
    }

}