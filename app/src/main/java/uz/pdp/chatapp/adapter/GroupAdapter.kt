package uz.pdp.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.pdp.chatapp.R
import uz.pdp.chatapp.databinding.ItemUserBinding
import uz.pdp.chatapp.modles.Group
import uz.pdp.chatapp.modles.GroupData
import uz.pdp.chatapp.modles.Message

class GroupAdapter(val list: ArrayList<Group>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<GroupAdapter.Vh>() {
    inner class Vh(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(groupData: Group, position: Int) {
            binding.onlineCard.visibility = View.INVISIBLE
            binding.image.setImageResource(R.drawable.group)
            binding.username.text = groupData.getName().toString()
            FirebaseDatabase.getInstance().getReference("groups").child(groupData.getId().toString())
                .child("messagers")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var messageList = ArrayList<Message>()
                        val children = snapshot.children
                        for (child in children) {
                            val value = child.getValue(Message::class.java)
                            if (value != null) {
                                messageList.add(value)
                            }
                        }
                        if (messageList.size >= 0) {
                            binding.messageUser.text =
                                messageList[messageList.size - 1].getMessage().toString()
                            binding.time1.text =
                                messageList[messageList.size - 1].getTime().toString()
                        } else {
                            binding.messageUser.text = ""
                            binding.time1.text = ""
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            binding.userItem.setOnClickListener {
                listener.onItemClick(groupData, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(groupData: Group, position: Int)
    }
}