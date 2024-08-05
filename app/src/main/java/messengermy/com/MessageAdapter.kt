package messengermy.com

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, val messagelist: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val Item_Recieve = 1
    val Item_Send = 2


    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var sentMessage = itemView.findViewById<TextView>(R.id.tv_sentmessage)

    }

    class RecieveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.tv_recievemessage)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            //inflate recieve

            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve_message , parent,false)
            return RecieveViewHolder(view)
        }else {
            // inflate send

            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messagelist[position]

        if (holder.javaClass == SentViewHolder::class.java){
            //code sent message holder
            val viewHolder =  holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }

        else{
            //recieve viewholder code
            val viewHolder =  holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message
        }


    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagelist[position]

         if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
           return Item_Send
        }else {
           return Item_Recieve

        }

    }

    override fun getItemCount(): Int {
        return messagelist.size
    }


}
