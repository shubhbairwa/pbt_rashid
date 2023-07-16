package com.paybacktraders.paybacktraders.activity

import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.databinding.ActivityAddClientBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.ResponseLogin
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class AddClientActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddClientBinding
    var RESULT_LOAD_IMAGE = 101
    var productId = 1
    lateinit var file: File
    var picturePath: String = ""
    lateinit var apis: Apis
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7
    var brokerList = arrayListOf<String>("EXNESS", "Vantage", "Fxopulence", "Multibank", "All")
    var brokerName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productId = intent.getIntExtra(Global.ID, 1)
        binding.btnChooseFile.setOnClickListener {
            checkAndRequestPermissions()
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }
        binding.closeDilogBt.setOnClickListener {
            finish()
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, brokerList)
        binding.acBrokerName.setAdapter<ArrayAdapter<String>>(adapter)

        binding.acBrokerName.setOnItemClickListener { parent, view, position, id ->
            brokerName = brokerList[position]
        }

        binding.btnSave.setOnClickListener {
            if (confirmInput(
                    binding.etFullName,
                    binding.etTradingAcc,
                    binding.etEmail,
                    binding.etPhoneNumber,
                    binding.etPassword,
                    binding.etTotalEquality,
                    brokerName,
                    picturePath
                )
            ) {
                Global.showDialog(this)
                apiHit()
                // Toast.makeText(this, "qweryu", Toast.LENGTH_SHORT).show()
            } else {
                // Toast.makeText(this, "NNNA", Toast.LENGTH_SHORT).show()
            }
            //
        }

        binding.clearData.setOnClickListener {
            clearFieldData()
        }

    }


    private fun confirmInput(
        fullName: TextInputEditText, tradingAc: TextInputEditText, enterEmail: TextInputEditText,
        enterPhone: TextInputEditText, tradingPass: TextInputEditText,
        totalequity: TextInputEditText, broker: String, picture: String
    ): Boolean {
        if (fullName.text!!.isEmpty()) {
            fullName.requestFocus()
            fullName.error = "invalid"
            return false
        } else if (tradingAc.text!!.isEmpty()) {
            tradingAc.requestFocus()
            tradingAc.error = "invalid"
            return false
        } else if (enterEmail.text!!.isEmpty()) {
            enterEmail.requestFocus()
            enterEmail.error = "invalid"
            return false
        } else if (enterPhone.text!!.isEmpty()) {
            enterPhone.requestFocus()
            enterPhone.error = "invalid"
            return false
        } else if (tradingPass.text!!.isEmpty()) {
            tradingPass.requestFocus()
            tradingPass.error = "invalid"
            return false
        } else if (totalequity.text!!.isEmpty()) {
            totalequity.requestFocus()
            totalequity.error = "invalid"
            return false
        } else if (broker.isEmpty()) {
            binding.acBrokerName.requestFocus()
            binding.acBrokerName.error = "invalid"
            return false
        } else if (picturePath.isEmpty()) {
            binding.etFileName.requestFocus()
            binding.etFileName.error = "invalid"
            return false
        } else {

            return true
        }
    }

    private fun clearFieldData() {
        binding.etFullName.setText("")
        binding.etEmail.setText("")
        binding.etPhoneNumber.setText("")
        binding.etTradingAcc.setText("")
        binding.etPassword.setText("")
        binding.etTotalEquality.setText("")
        binding.acBrokerName.setText("")
    }

    //todo api hitting here...
    fun apiHit() {
        val builder = MultipartBody.Builder()
      //  val part: MultipartBody.Part = MultipartBody.Part.createFormData("int", "123")
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("FullName", binding.etFullName.text.toString())
        builder.addFormDataPart("Email", binding.etEmail.text.toString())
        builder.addFormDataPart("Mobile", binding.etPhoneNumber.text.toString())
        builder.addFormDataPart("TradingAcNo", binding.etTradingAcc.text.toString())
        builder.addFormDataPart("TradingAcPass", binding.etPassword.text.toString())
        builder.addFormDataPart("TotalEquity", binding.etTotalEquality.text.toString())
        builder.addFormDataPart("CreatedBy", Prefs.getInt(Global.ID).toString())
        builder.addFormDataPart("BrokerName", brokerName)
        builder.addFormDataPart("ProductId", productId.toString())
        //builder.addPart(part)

        //todo convert multi image array list into multipartBody.part form....
        val file: File
        try {
            file = File(picturePath)
            builder.addFormDataPart(
                "PaymentProof",
                file.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            )
        } catch (e: Exception) {
            builder.addFormDataPart(
                "PaymentProof",
                "",
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            )
            e.printStackTrace()
        }

        var requestBody = builder.build()
        Log.e("payload--->", requestBody.toString())

        apis =
            ApiClient().service // UploadNetworkClient.retrofitInstance!!.create(Apis::class.java)
        val call: Call<ResponseLogin> = apis.customerCreate(requestBody).apply {
            enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    try {
                        if (response.isSuccessful) {
                            Global.hideDialog()
                            Log.d("responseSuccess==>", response.body().toString())
                            var responseResult = response.body()
                            if (responseResult?.status == 200) {
                                Log.d("responseResult===>", responseResult.message)
                                Toasty.success(
                                    this@AddClientActivity,
                                    "Successful",
                                    Toasty.LENGTH_SHORT
                                ).show()
                                /* var intent = Intent(this@AddClientActivity, DashBoardFragment::class.java)
                                 startActivity(intent)*/
                                finish()
                                // onBackPressed()
                            } else {
                                Toasty.error(
                                    this@AddClientActivity,
                                    responseResult?.message!!,
                                    Toasty.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Log.d("responseError==>", response.message())
                            Toasty.error(
                                this@AddClientActivity,
                                "Something Wrong!",
                                Toasty.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {
                        Global.hideDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    Global.hideDialog()
                    Toasty.error(this@AddClientActivity, t.message!!, Toasty.LENGTH_SHORT).show()
                }

            })
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_LOAD_IMAGE -> if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                val extras: Bundle? = data?.getExtras()
                val selectedImage: Uri? = data!!.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
                picturePath = cursor.getString(columnIndex)
                if (cursor != null) {
                    cursor.close()
                }
                Log.e("picturePath", picturePath.toString())
                binding.ivSetImage.setImageURI(Uri.parse(picturePath))
                var file = File(picturePath)
                binding.etFileName.setText(file.name)
            }
        }
    }

    //todo set permission for image...
    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val wtite =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("in fragment on request", "Permission callback called-------")
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    // Check for both permissions
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(
                            "in fragment on request",
                            "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted"
                        )
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(
                            "in fragment on request",
                            "Some permissions are not granted ask again "
                        )
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showDialogOK("Camera and Storage Permission required for this app") { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

}