package com.paybacktraders.paybacktraders.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.paybacktraders.paybacktraders.databinding.EditLayoutBinding

class DialogEditClient:DialogFragment() {
    lateinit var binding:EditLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding= EditLayoutBinding.inflate(layoutInflater)



        return super.onCreateDialog(savedInstanceState)
    }



}