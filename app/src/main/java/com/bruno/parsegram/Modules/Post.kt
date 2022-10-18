package com.bruno.parsegram.Modules

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser



@ParseClassName("Post")
class Post: ParseObject() {
    fun getDescription():String?{ return getString(KEY_DESCRIPTION) }
    fun setDescription(description:String){ put(KEY_DESCRIPTION,description) }

    fun getImage():ParseFile?{ return getParseFile(KEY_IMAGE) }
    fun setImage(image:ParseFile){ put(KEY_IMAGE,image) }

    fun setUser(user:ParseUser){ put(KEY_USER,user) }
    fun getUser():ParseUser?{ return getParseUser(KEY_USER) }
    companion object{
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }

}