package com.bruno.parsegram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bruno.parsegram.Modules.Post
import com.bumptech.glide.Glide

class PostAdapter(val context:Context,val posts:List<Post>):RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvUsername:TextView
        val ivImage: ImageView
        val tvDescription: TextView
        init{
            tvUsername = itemView.findViewById(R.id.tvProfileName)
            ivImage = itemView.findViewById(R.id.ivProfileImage)
            tvDescription = itemView.findViewById(R.id.tvDescription)
        }
        fun bind(post: Post){
            tvDescription.text=post.getDescription()
            tvUsername.text=post.getUser()?.username
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
        }
    }
}