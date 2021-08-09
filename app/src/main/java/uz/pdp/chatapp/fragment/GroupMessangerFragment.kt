package uz.pdp.chatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.pdp.chatapp.R
import uz.pdp.chatapp.adapter.GroupMessageAdapter
import uz.pdp.chatapp.adapter.UserAdapter
import uz.pdp.chatapp.databinding.FragmentGroupMessangerBinding
import uz.pdp.chatapp.modles.GroupData
import uz.pdp.chatapp.modles.GroupMessage
import uz.pdp.chatapp.modles.Message
import uz.pdp.chatapp.modles.UserData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupMessangerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentGroupMessangerBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var userAdapter: GroupMessageAdapter
    lateinit var messageList: ArrayList<GroupMessage>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentGroupMessangerBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("groups")
        messageList = ArrayList()
        val currentUser = firebaseAuth.currentUser
        binding.online.text = "24 users"
        binding.userName.text = param2.toString()
        binding.backHome.setOnClickListener { findNavController().popBackStack() }
        binding.sendBtn.setOnClickListener {
            if (binding.message.text.isEmpty()) {
                Toast.makeText(binding.root.context, "Do not text!!!", Toast.LENGTH_SHORT).show()
            } else {
                val message = binding.message.text.toString()
                val date = Date()
                val simpleDataFormat = SimpleDateFormat("HH:mm")
                val dateTime = simpleDataFormat.format(date)
                val image_url =
                    "https://lh3.googleusercontent.com/a-/AOh14GisQN1m6HtjZ8PybTFkllFkQaDTrvpO6_B9F_svuw=s96-c"
                val timeKey = System.currentTimeMillis()
                val messageData = GroupMessage(message,
                    currentUser!!.photoUrl.toString(),
                    image_url,
                    dateTime,
                    param2,
                    currentUser.displayName)
                reference.child(param1!!).child("messagers").child(timeKey.toString())
                    .setValue(messageData)
            }
            binding.message.text.clear()
        }
        reference.child(param1!!).child("messagers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    val currentUser = firebaseAuth.currentUser
                    val children = snapshot.children
                    for (child in children) {
                        var value = child.getValue(GroupMessage::class.java)
                        if (value != null) {
                            messageList.add(value)
                        }
                    }
                    userAdapter = GroupMessageAdapter(messageList, currentUser?.photoUrl.toString())
                    binding.rv.adapter = userAdapter
                    binding.rv.scrollToPosition(messageList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            GroupMessangerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}