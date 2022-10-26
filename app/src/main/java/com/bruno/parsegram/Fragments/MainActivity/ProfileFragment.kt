package com.bruno.parsegram.Fragments.MainActivity

import android.util.Log
import com.bruno.parsegram.Modules.Post
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment:FeedFragment() {
    override fun queryForPosts(){
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.whereEqualTo(Post.KEY_USER,ParseUser.getCurrentUser())
        query.addDescendingOrder("createdAt")
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching posts "+e)
            } else {
                if (posts != null) {
                    for (post in posts) {
                        Log.i(
                            TAG, "Post:  " + post.getDescription()+ " ,username: "+
                                    post.getUser()?.username)
                    }
                }
                allPosts.addAll(posts)
                adapter.notifyDataSetChanged()
            }
        }
    }
}