package com.luqmanfajar.story_app.utils

import android.app.Dialog
import android.content.Context
import com.luqmanfajar.story_app.R

object UiUtils {
    private lateinit var dialog: Dialog
    fun show(context: Context) {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.item_dialog)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun close() {
        dialog.dismiss()
    }
}