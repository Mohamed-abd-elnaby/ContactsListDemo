package com.example.halatask.features.contactDetails

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.halatask.BuildConfig
import com.example.halatask.R
import com.example.halatask.base.BaseFragment
import com.example.halatask.databinding.ContactDetailsBinding
import com.example.halatask.entities.users.UserContact

class ContactDetailsScreen : BaseFragment() {
    private lateinit var bind: ContactDetailsBinding
    private var userContact: UserContact? = null
    override fun initialComponent() {
        ContactDetailsScreenArgs.fromBundle(requireArguments()).userContact.let {
            userContact = it
            updateUI()
        }
    }

    override fun getPageName(): String {
        return getString(R.string.contactDetailsScreenName)
    }

    private fun updateUI() {
        bind.apply {
            userContact?.let { userContact ->
                tvName.text = userContact.getFullName()
                tvAddressValue.text = userContact.contact?.address
                tvBio.text = userContact.bio
                tvBirthDateValue.text = userContact.birth_day
                tvCompanyValue.text = userContact.job?.company
                tvCountryValue.text = userContact.country
                tvRoleValue.text = userContact.job?.role
                tvPhoneValue.text = userContact.contact?.phone_number
                tvEmailValue.text = userContact.contact?.email
                tvEducationValue.text = userContact.education
                if (userContact.image != null) {
                    val image: Drawable = BitmapDrawable(
                        root.context.resources,
                        BitmapFactory.decodeByteArray(
                            userContact.image,
                            0,
                            userContact.image?.size ?: 0
                        )
                    )
                    imageView.setImageDrawable(image)
                }
            }
        }
    }

    override fun clicks() {
        bind.apply {
            ivCall.setOnClickListener {
                userContact?.contact?.phone_number?.let { phone ->
                    callContact(phone)
                }
            }

            ivEmail.setOnClickListener {
                userContact?.contact?.email?.let { email ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email"))
                    startActivity(intent)
                }
            }
        }
    }

    override fun getInflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        bind = ContactDetailsBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun getStates(): List<LiveData<*>> {
        return emptyList()
    }

    override fun invoke(p1: Any?) {

    }

    private fun callContact(number: String) {
        requestPermission(Manifest.permission.CALL_PHONE, getString(R.string.callMustEnabled)) {
            if (it) {
                try {
                    val newIntent =
                        Intent(Intent.ACTION_CALL, Uri.parse("tel:${number}"))
                    startActivity(newIntent)
                } catch (Ex: Exception) {
                    if (BuildConfig.DEBUG)
                        Ex.printStackTrace()
                }
            }
        }
    }

    private fun openUrl(url: String) {
        val newIntent = Intent(Intent.ACTION_VIEW)
        newIntent.data = Uri.parse(url)
        startActivity(newIntent)
    }

}