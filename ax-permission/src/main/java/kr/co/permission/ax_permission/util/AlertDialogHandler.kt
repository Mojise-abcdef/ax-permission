package kr.co.permission.ax_permission.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AlertDialogHandler(private val context: Context) {

    fun showDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClick: ((dialog: AlertDialog) -> Unit)? = null,
        onNegativeClick: ((dialog: AlertDialog) -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(positiveButtonText) { _, _ ->
            val dialog = builder.create()
            onPositiveClick?.invoke(dialog)
        }

        builder.setNegativeButton(negativeButtonText) { _, _ ->
            val dialog = builder.create()
            onNegativeClick?.invoke(dialog)
        }

        val dialog = builder.create()
        dialog.show()
    }
}