package com.paybacktraders.paybacktraders.activity.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.activity.ui.forgot.ForgotPasswordActivity
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentProfileBinding
import com.paybacktraders.paybacktraders.fragments.forgotScreenfragment.ChangePasswordFragment
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import java.lang.String
import java.util.*

class ProfileFragment : Fragment() {

    //todo comments 22

    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NavigationDrawerActivity).viewModel

       /* binding.toolbarProfile.setOnClickListener {
            (activity as NavigationDrawerActivity).finish()
        }*/

        if (Prefs.getInt(Global.ID) != null || Prefs.getInt(Global.ID) != 0) {
            var jsonObject = JsonObject()
            jsonObject.addProperty("id", Prefs.getInt(Global.ID))
            viewModel.getProfileDetailOneApi(jsonObject)
        }

        bindToObserver()

        binding.editProfileLc.setOnClickListener {
            var intent = Intent(requireActivity(), EditProfileActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.changePasswordLc.setOnClickListener {
            switchFragmentWithData(ChangePasswordFragment(), "DataForProfileUpdateScreen")
        }

    }

    override fun onResume() {
        super.onResume()
    }

    //todo bind observer.
    private fun bindToObserver() {
        viewModel.profileDeatil.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(requireActivity(),it, Toasty.LENGTH_SHORT).show()
            },
            onLoading = {
                Global.showDialog(requireActivity())
            }, onSuccess = {
                    response ->
                Global.hideDialog()
                if (response.status == 200){
                    setData(response.data[0])
                }else {
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    //todo set default data..
    private fun setData(data: DataEmployeeAll) {
        binding.tvUserId.text = data.id.toString()
        binding.tvUsername.text = data.FullName
        binding.tvEmail.text = data.Email
        binding.tvPhone.text = data.Mobile
        binding.tvDeliveryAddress.text = data.DeliveryAddress

        binding.tvCharacterOfImageView.setText(String.valueOf(data.FullName[0]).uppercase(Locale.getDefault()))
    }

    // Function to switch between fragments with data parse..
    fun switchFragmentWithData(fragment: Fragment, dataToSend: kotlin.String) {
        val args = Bundle()
        args.putString("key_data", dataToSend)
        fragment.arguments = args

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_content_navigation_drawer, fragment)
        transaction.addToBackStack(null) // Add to back stack to enable back navigation
        transaction.commit()
    }

}