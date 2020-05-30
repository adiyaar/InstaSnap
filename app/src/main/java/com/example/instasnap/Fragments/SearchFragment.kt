package com.example.instasnap.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instasnap.Adapter.UserAdapter

import com.example.instasnap.R
import com.example.instasnap.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment() {

    private var recyclerView: RecyclerView?= null
    private var userAdapter: UserAdapter?=null
    private var muser: MutableList<User>?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        muser = ArrayList()
        userAdapter = context?.let { UserAdapter(it , muser as ArrayList<User> , true) }
        recyclerView?.adapter = userAdapter

        view.search_edit_text.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(view.search_edit_text.text.toString() == "")
                {

                }
                else
                {
                    recyclerView?.visibility = View.VISIBLE
                    retrieveusers()
                    searchUser(s.toString().toLowerCase())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return view
    }

    private fun retrieveusers() {
        val userRef = FirebaseDatabase.getInstance().getReference().child("Users")
        userRef.addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(view?.search_edit_text?.text.toString() == "")
                {
                    muser?.clear()

                    for(snapshot in dataSnapshot.children)
                    {
                        val user = snapshot.getValue(User::class.java)
                        if(user!= null)
                        {
                            muser?.add(user)
                        }
                    }

                    userAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun searchUser(input: String)
    {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .orderByChild("fullname")
            .startAt(input)
            .endAt(input+ "\uf8ff")

        query.addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                muser?.clear()

                for(snapshot in dataSnapshot.children)
                {
                    val user = snapshot.getValue(User::class.java)
                    if(user!= null)
                    {
                        muser?.add(user)
                    }
                }


            }
    })

}}
