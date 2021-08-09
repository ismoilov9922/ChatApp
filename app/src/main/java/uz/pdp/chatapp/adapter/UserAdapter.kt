package uz.pdp.chatapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import uz.pdp.chatapp.databinding.ItemUserBinding
import uz.pdp.chatapp.modles.Message
import uz.pdp.chatapp.modles.UserData

class UserAdapter(
    val list: ArrayList<UserData>,
    val currentUser: String,
    val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<UserAdapter.Vh>() {
    inner class Vh(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(userData: UserData, position: Int) {
            Picasso.get().load(userData.getImageUrl()).into(binding.image)
            binding.username.text = userData.getUsername().toString()
            FirebaseDatabase.getInstance().getReference("Users").child(userData.getUid().toString())
                .child("messages").child(currentUser)
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
                        if (messageList.size > 0) {
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

            if (userData.getOnline() == true) {
                binding.online.setCardBackgroundColor(Color.GREEN)
            }
            binding.userItem.setOnClickListener {
                listener.onItemClick(userData, position)
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
        fun onItemClick(userData: UserData, position: Int)
    }
}