package kr.co.permission.ax_permission.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kr.co.permission.ax_permission.R

class AlertDialogHandler(private val context: Context) {

    fun showDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClick: ((dialog: AlertDialog) -> Unit)? = null,
        onNegativeClick: ((dialog: AlertDialog) -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(positiveButtonText) { dialogInterface, _ ->
            onPositiveClick?.invoke(dialogInterface as AlertDialog)
        }

        builder.setNegativeButton(negativeButtonText) { dialogInterface, _ ->
            onNegativeClick?.invoke(dialogInterface as AlertDialog)
        }

        val dialog = builder.create()
        dialog.show()

        // Set button text colors programmatically
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val isDarkMode = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val buttonTextColor = if (isDarkMode) {
            ContextCompat.getColor(context, R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.black)
        }

        positiveButton.setTextColor(buttonTextColor)
        negativeButton.setTextColor(buttonTextColor)
    }
}