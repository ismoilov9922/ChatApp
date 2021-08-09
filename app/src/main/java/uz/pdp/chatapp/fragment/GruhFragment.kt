package uz.pdp.chatapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.pdp.chatapp.R
import uz.pdp.chatapp.adapter.GroupAdapter
import uz.pdp.chatapp.databinding.FragmentGruhBinding
import uz.pdp.chatapp.databinding.GroupDialogBinding
import uz.pdp.chatapp.modles.Group
import uz.pdp.chatapp.modles.GroupData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GruhFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentGruhBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var reference: DatabaseReference
    lateinit var groupList: ArrayList<Group>
    lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentGruhBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("groups")
        binding.addGroup.setOnClickListener {
            var alertDialog = AlertDialog.Builder(binding.root.context)
            val addItemBinding = GroupDialogBinding.inflate(layoutInflater)
            alertDialog.setView(addItemBinding.root)

            alertDialog.setPositiveButton("Create") { d, V ->
                val name = addItemBinding.groupName.text.toString()
                val info = addItemBinding.aboutGroup.text.toString()
                var time = System.currentTimeMillis().toString()
                var group = GroupData(time, name, info)
                reference.child(group.getId()!!).setValue(group)
            }
            alertDialog.setNegativeButton("Cancel") { d, v ->
                alertDialog.create().dismiss()
            }
            alertDialog.show()
        }
        groupList = ArrayList()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                val children = snapshot.children
                for (child in children) {
                    var value = child.getValue(Group::class.java)
                    if (value != null) {
                        groupList.add(value)
                    }
                }
                groupAdapter = GroupAdapter(groupList, object : GroupAdapter.OnItemClickListener {
                    override fun onItemClick(groupData: Group, position: Int) {
                        val bundle = Bundle()
                        bundle.putString(ARG_PARAM1, groupData.getId())
                        bundle.putString(ARG_PARAM2, groupData.getName())
                        findNavController().navigate(R.id.groupMessangerFragment, bundle)
                    }
                })
                binding.rvGroup.adapter = groupAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}



