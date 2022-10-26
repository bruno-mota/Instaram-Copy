package com.bruno.parsegram.Fragments.MainActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bruno.parsegram.Modules.Post
import com.bruno.parsegram.PostAdapter
import com.bruno.parsegram.R
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts:MutableList<Post> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsRecyclerView = view.findViewById(R.id.rv_posts)
        adapter = PostAdapter(requireContext(),allPosts)
        postsRecyclerView.adapter=adapter
        postsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        queryForPosts()

    }
    open fun queryForPosts() {
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
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
    companion object{
        const val TAG="FeedFragment"
    }
}