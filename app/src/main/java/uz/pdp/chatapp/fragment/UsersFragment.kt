package uz.pdp.chatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.pdp.chatapp.R
import uz.pdp.chatapp.adapter.MessageRvAdapter
import uz.pdp.chatapp.adapter.UserAdapter
import uz.pdp.chatapp.databinding.FragmentUsersBinding
import uz.pdp.chatapp.modles.Message
import uz.pdp.chatapp.modles.UserData
import uz.pdp.chatapp.modles.UserData1

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UsersFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentUsersBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<UserData>
    lateinit var newMesssageList: ArrayList<Message>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUsersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users")
        userList = ArrayList()
        newMesssageList = ArrayList()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = firebaseAuth.currentUser
                val children = snapshot.children
                for (child in children) {
                    var value = child.getValue(UserData::class.java)
                    if (value != null && currentUser?.uid != value.getUid()) {
                        userList.add(value)
                    }
                }
                userAdapter = UserAdapter(userList, currentUser!!.uid,object : UserAdapter.OnItemClickListener {
                    override fun onItemClick(userData: UserData, position: Int) {
                        val bundle = Bundle()
                        bundle.putSerializable(ARG_PARAM1, userData)
                        findNavController().navigate(R.id.messengerFragment, bundle)
                    }
                })
                binding.rv.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users")
        reference.child("${currentUser?.uid}").child("online").setValue(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users")
        reference.child("${currentUser?.uid}").child("online").setValue(false)
    }
}

