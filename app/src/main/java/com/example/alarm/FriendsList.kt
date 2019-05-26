package com.example.alarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_friends_list.*
import org.json.JSONArray
import com.parse.*
import com.parse.ParseObject
import com.parse.GetCallback
import com.parse.ParseQuery
import com.parse.FindCallback
import org.json.JSONObject


class FriendsList : Fragment() {

    lateinit var list: Array<String?>
    var listOfFriends = HashSet<String>()
    var listOfAll = ArrayList<String>()
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //test()

        return inflater!!.inflate(R.layout.activity_friends_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        findFollowers()

        //list = Array(5){i -> i.toString()}
        list = Array(0) { i -> i.toString()}
        //listOfFriends.toArray(list)
        adapter = ArrayAdapter<String>(
            this.context,
            android.R.layout.simple_list_item_1,
            list
        )
        listview.adapter = adapter
        listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, positon, id ->
            val intentfriend = Intent(this.context, FriendInfo::class.java)
            intentfriend.putExtra("username", list[positon])
            startActivity(intentfriend)
            true
        }

        listview.setOnItemLongClickListener {  parent, view, position, id ->
            // Get the selected item text from ListView


            deleteFollower(ParseUser.getCurrentUser().objectId, list[position].toString())


                        true
        }
        //list.set(0, "Domi")
        //adapter.notifyDataSetChanged()
        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(text: String): Boolean {
                val query = ParseUser.getQuery()
                query.whereEqualTo("username", text)
                query.findInBackground { users, e ->
                    if (e == null) {
                        // The query was successful, returns the users that matches
                        // the criterias.
                        for (user in users) {
                            list = Array(users.size) { i -> users.get(i).username }
                            adapter = ArrayAdapter<String>(
                                this@FriendsList.context,
                                android.R.layout.simple_list_item_1,
                                list
                            )
                            listview.adapter = adapter
                            println(user.username)
                        }
                    } else {
                        // Something went wrong.
                    }
                }
                adapter.filter.filter(text)
                return true
            }

        })

    }
/*
    fun findUsers() {
        val parametersForAllUsers = HashMap<String, String>()
        parametersForAllUsers.put("user", ParseUser.getCurrentUser().objectId.toString())
        ParseCloud.callFunctionInBackground("getAllUsers", parametersForAllUsers,
            FunctionCallback<ArrayList<Any>> { followers, e ->
                if (e == null) {
                    System.out.println("NIEMA")

                    var iterator = 0
                    for (i in 0 until followers.size) {
                        var jsonArray = JSONArray(followers.toString())
                        for (j in 0 until jsonArray.length()) {

                            if (!jsonArray.getJSONObject(j).get("objectId").toString().equals(ParseUser.getCurrentUser().objectId.toString())) {
                                listOfAll.add(jsonArray.getJSONObject(j).get("username").toString())
                                System.out.println(listOfAll.get(iterator).toString())
                                iterator++;
                            }

                        }
                    }
                } else {
                    // Something went wrong
                    System.out.println("something went wrong : " + e + " " + e.message)
                }
            })
    }
*/

    fun findFollowers() {
        val parametersForFollowers = HashMap<String, String>()
        parametersForFollowers.put("user", ParseUser.getCurrentUser().objectId.toString())
        ParseCloud.callFunctionInBackground("getFollowers", parametersForFollowers,
            FunctionCallback<ArrayList<Any>> { followers, e ->
                if (e == null) {
                    var channels = ""
                //    for (i in 0 until followers.size)

                        var jsonArray = JSONArray(followers.toString())
                        for (j in 0 until jsonArray.length()) {
                            listOfFriends.add(jsonArray.getJSONObject(j).get("username").toString())
                            channels += jsonArray.getJSONObject(j).get("username").toString() + " "

                        }

                    list = Array(listOfFriends.size) { i -> listOfFriends.elementAt(i) }
                    adapter.notifyDataSetChanged()

                    adapter = ArrayAdapter<String>(
                        this.context,
                        android.R.layout.simple_list_item_1,
                        list
                    )
                    listview.adapter = adapter




                }
            })
    }

    fun deleteFollower(user1 : String, user2 : String) {
        val parametersForFollowers = HashMap<String, String>()
        var usernameOfFollower = ""
        parametersForFollowers.put("user1", ParseUser.getCurrentUser().objectId.toString())
        val query = ParseUser.getQuery()
        query.whereEqualTo("username", user2)
        query.findInBackground { users, e ->
            if (e == null) {
                // The query was successful, returns the users that matches
                // the criterias.
                for (user in users) {
                    if(user.objectId != "")
                    {
                        usernameOfFollower = user.objectId.toString()
                        System.out.println( "USER2"+ usernameOfFollower)
                    }

                    println(user.username)
                }
                parametersForFollowers.put("user2", usernameOfFollower)

                System.out.println("jedzonko")
                ParseCloud.callFunctionInBackground("findFriendship", parametersForFollowers,
                    FunctionCallback<HashMap<Any,Any>> { friendship, e ->
                        if (e == null) {

                            val query = ParseQuery.getQuery<ParseObject>("Friends")
                            var json = JSONObject(friendship.toString())
                            var friendshipId = json.get("id").toString()
                            System.out.println("friendshipId "+friendshipId)
                            // Retrieve the object by id
                            query.getInBackground(friendshipId) { entity, e ->
                                if (e == null) {
                                    // Otherwise, you can delete the entire ParseObject from the database
                                    entity.deleteInBackground()
                                    findFollowers()

                                    Toast.makeText(this.context, "Użytkownik "+user2+" został usunięty z listy znajomych.", Toast.LENGTH_LONG).show()



                                }
                                else
                                {
                                    System.out.println("else wewnątrz wewnątrz : "+e.message)

                                }
                            }

                        }
                        else
                        {

                            System.out.println("else wewnątrz : "+e.message)
                        }
                    })
            } else {
                // Something went wrong.
                System.out.println("else zewnątrz : "+e.message)

            }
        }

    }

    override fun onResume() {
        super.onResume()
        findFollowers()
    }

}