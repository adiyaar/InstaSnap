package com.example.instasnap.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instasnap.Fragments.ProfileFragment
import com.example.instasnap.R
import com.example.instasnap.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_item_layout.view.*

class UserAdapter(private var mContext:Context,
                   private var muser: List<User>,
                    private  var isFragment: Boolean = false ): RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout , parent , false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return muser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
            val user = muser[position]
        holder.userNameTextView.text = user.getUsername()
        holder.userFullanmeTextView.text = user.getFullname()
        Picasso.get().load(user.getimage()).placeholder(R.drawable.profile).into(holder.userProfileImage)
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var  userNameTextView : TextView = itemView.findViewById(R.id.user_name_search)
        var  userFullanmeTextView : TextView = itemView.findViewById(R.id.user_full_name_search)
        var  userProfileImage : CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var  followbutton : Button = itemView.findViewById(R.id.follow_btn_search)
    }
}