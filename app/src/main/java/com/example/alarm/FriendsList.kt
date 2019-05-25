package com.example.alarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_friends_list.*

class FriendsList : Fragment() {

    val list = arrayOf("Ania", "Lolek","Hipis","stroznocny","123ala","kaczka","Agata")
    lateinit var adapter  : ArrayAdapter <String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater!!.inflate(R.layout.activity_friends_list, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = ArrayAdapter<String>(
            this.context,
            android.R.layout.simple_list_item_1,
            list
        )
        listview.adapter = adapter
        listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, positon, id ->
            val intentfriend = Intent(this.context, FriendInfo::class.java)
            intentfriend.putExtra("username",list[positon])
            startActivity(intentfriend)
            true
        }

        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return true
            }

        })

    }
}