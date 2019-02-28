package com.example.lenovo.myproject.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.lenovo.myproject.MainActivity
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.fragments.ProfileFragment

class DialogSaving : DialogFragment(), DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, which: Int) {
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.stay_button) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.leave_button) { _, _ ->
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, ProfileFragment.newInstance())
                ?.commit()
        }
        builder.setMessage(this.arguments?.getString(MainActivity.ARG_MESSAGE))
        return builder.create()
    }
}