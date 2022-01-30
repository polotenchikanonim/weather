package local.kas.weather.view.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import local.kas.weather.databinding.ContactsFragmentBinding


class ContactsFragment : Fragment() {

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        requireContext().let {
            when {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    println()
                }
            }
        }

    }

    private fun getContacts() {
        requireContext().contentResolver.let {
            it.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null
            )?.let { cur ->
                while (cur.moveToNext()) {
                    val id =
                        cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val name =
                        cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    if (cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt() > 0
                    ) {
                        it.query(
                            Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + id,
                            null, null
                        )?.let { phone ->
                            while (phone.moveToNext()) {
                                val phoneNumber =
                                    phone.getString(phone.getColumnIndexOrThrow(Phone.NUMBER))
                                addContact(name, phoneNumber)
                            }
                            phone.close()
                        }
                    }
                }
                cur.close()
            }
        }
    }

    private fun addContact(name: String, phone: String) {
        binding.contactsLL.addView(TextView(requireContext()).apply {
            val output = "$name $phone"
            text = output
        })
    }

    private fun checkPermission() {
        requireContext().let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    requestPermission()
                }
            }


        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 0)
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам").setMessage("message")
            .setPositiveButton("предоставить доступ") { _, _ -> requestPermission() }
            .setNegativeButton("no") { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

}