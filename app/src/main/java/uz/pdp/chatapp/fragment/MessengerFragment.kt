package uz.pdp.chatapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import uz.pdp.chatapp.adapter.MessageRvAdapter
import uz.pdp.chatapp.databinding.DialogImageBinding
import uz.pdp.chatapp.databinding.FragmentMessengerBinding
import uz.pdp.chatapp.modles.Message
import uz.pdp.chatapp.modles.UserData
import uz.pdp.chatapp.modles.UserData1
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"

class MessengerFragment : Fragment() {
    private var userData: UserData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userData = it.getSerializable(ARG_PARAM1) as UserData
        }
    }

    lateinit var binding: FragmentMessengerBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var messageList: ArrayList<Message>
    lateinit var messageAdapter: MessageRvAdapter
    private val TAG = "Message"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMessengerBinding.inflate(layoutInflater)
        binding.imageUser.setOnClickListener {
            val alertDialog = AlertDialog.Builder(binding.root.context)
            val dialogBinding = DialogImageBinding.inflate(layoutInflater)
            alertDialog.setView(dialogBinding.root)
            Picasso.get().load(userData?.getImageUrl()).into(dialogBinding.imageUser)
            alertDialog.show()
        }
        binding.backHome.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (userData != null) {
            if (userData?.getOnline() == true) {
                binding.online.text = "online"
            }else{
                binding.online.text = "last seen recently"
            }
        }
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users")
        val currentUser = auth.currentUser
        Picasso.get().load(userData?.getImageUrl()).into(binding.imageUser)
        binding.userName.text = userData?.getUsername()
        binding.userName.isSelected = true
        binding.sendBtn.setOnClickListener {
            if (binding.message.text.isEmpty()) {
                Toast.makeText(binding.root.context, "Do not text!!!", Toast.LENGTH_SHORT).show()
            } else {
                val message = binding.message.text.toString()
                val date = Date()
                val simpleDataFormat = SimpleDateFormat("HH:mm")
                val dateTime = simpleDataFormat.format(date)
                val messageData = Message(message, currentUser!!.uid, userData?.getUid(), dateTime)
                val key = reference.push().key
                reference.child("${currentUser.uid}/messages/${userData?.getUid()}/$key")
                    .setValue(messageData)
                reference.child("${userData?.getUid()}/messages/${currentUser.uid}/$key")
                    .setValue(messageData)
            }
            binding.message.text = null
        }
        messageList = ArrayList()
        reference.child("${currentUser?.uid}/messages/${userData?.getUid()}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    val children = snapshot.children
                    for (child in children) {
                        val value = child.getValue(Message::class.java)
                        if (value != null) {
                            messageList.add(value)
                        }
                    }
                    messageAdapter = MessageRvAdapter(messageList,
                        currentUser?.uid!!,
                        currentUser.photoUrl!!,
                        userData?.getImageUrl()!!)
                    binding.rv.adapter = messageAdapter
                    binding.rv.scrollToPosition(messageList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}