package com.example.lenovo.myproject.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.lenovo.myproject.MainActivity
import com.example.lenovo.myproject.R

class DialogSaving : DialogFragment(), DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, which: Int) {
        dismiss()
    }

    interface DialogSavingListener {
        fun onLeaveDialogSavingButtonClicked()
    }

    private var listener: DialogSavingListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DialogSavingListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not DialogSavingListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.stay_button) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.leave_button) { _, _ ->
            listener?.onLeaveDialogSavingButtonClicked()
        }
        builder.setMessage(this.arguments?.getString(MainActivity.ARG_MESSAGE))
        return builder.create()
    }
}