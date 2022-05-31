package com.example.halatask.features.contactList

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.halatask.base.MainApp
import com.example.halatask.base.loadImage
import com.example.halatask.databinding.ContactItemBinding
import com.example.halatask.entities.users.UserContact


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ItemViewHolder>() {
    private var list: MutableList<UserContact> = mutableListOf()
    fun addNewContact(userContact: UserContact) {
        list.add(userContact)
        notifyItemChanged(list.indexOf(userContact))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val feedbackItem = list[position]
        holder.displayData(feedbackItem, position) { p1, p2 ->
            list[p2].image = p1
            notifyItemChanged(p2)
        }
    }


    class ItemViewHolder(private var bind: ContactItemBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun displayData(
            userContact: UserContact, position: Int, callback: (ByteArray?, position: Int) -> Unit
        ) {
            bind.apply {
                tvName.text = userContact.first_name
                tvRole.text = userContact.job?.role
                tvCompany.text = userContact.job?.company
                if (userContact.image != null) {
                    progress.visibility = View.GONE
                    val image: Drawable = BitmapDrawable(
                        root.context.resources,
                        BitmapFactory.decodeByteArray(
                            userContact.image,
                            0,
                            userContact.image?.size ?: 0
                        )
                    )
                    ivContact.setImageDrawable(image)
                } else {
                    ivContact.setImageDrawable(null)
                    progress.visibility = View.GONE
                    userContact.avatar?.let {
                        loadImage(root.context, it) { drawable ->
                            MainApp.mainApp.handler.post {
                                callback(drawable, position)
                            }
                        }
                    }

                }
                root.setOnClickListener {
                    root.findNavController().navigate(
                        ContactListScreenDirections.actionContactListScreenToContactDetailsScreen(
                            userContact
                        )
                    )
                }

            }

        }

    }
}