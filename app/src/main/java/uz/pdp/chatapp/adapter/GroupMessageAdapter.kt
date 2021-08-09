package uz.pdp.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.chatapp.databinding.FromMessageBinding
import uz.pdp.chatapp.databinding.ToMessageBinding
import uz.pdp.chatapp.modles.GroupMessage
import uz.pdp.chatapp.modles.Message

class GroupMessageAdapter(
    var list: ArrayList<GroupMessage>,var current_img:String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class Vh1(var fromMessageBinding: FromMessageBinding) :
        RecyclerView.ViewHolder(fromMessageBinding.root) {
        fun onBind(message: GroupMessage, position: Int) {
            fromMessageBinding.message.text = message.getMessage()
            fromMessageBinding.time.text = message.getTime()
            Picasso.get().load(message.getFromImg()).into(fromMessageBinding.fromImage)
        }
    }

    inner class Vh2(var toMessageBinding: ToMessageBinding) :
        RecyclerView.ViewHolder(toMessageBinding.root) {
        fun onBind(message: GroupMessage, position: Int) {
            toMessageBinding.message.text = message.getMessage()
            toMessageBinding.time.text = message.getTime()
            Picasso.get().load(message.getFromImg()).into(toMessageBinding.toImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return Vh1(
                FromMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return Vh2(
                ToMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val vh1 = holder as Vh1
            vh1.onBind(list[position], position)
        } else {
            val vh2 = holder as Vh2
            vh2.onBind(list[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].getFromImg() == current_img) {
            return 1
        }
        return 2
    }

    override fun getItemCount(): Int = list.size
}