package com.example.prodiatestapps

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebSettings.PluginState
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prodiatestapps.model.UploadRequestBody
import com.example.prodiatestapps.ui.main.UploadFilePresenter
import com.example.prodiatestapps.ui.main.UploadFileView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MultipartBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MainActivity : AppCompatActivity(), UploadRequestBody.UploadCallback, UploadFileView.InitView {
    private var selectedFile : Uri? = null
    private var presenter : UploadFilePresenter? = null
    private var isPdf: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = UploadFilePresenter(this)

        layoutPreview.setOnClickListener {
            showDialog()
        }

        buttonUpload.setOnClickListener {
            uploadFile()
        }
    }

    fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Choose File.")
        builder.setCancelable(true)

        builder.setPositiveButton(
                "PDF",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                    choosePdf()
                })

        builder.setNegativeButton(
                "Image",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                    chooseImage()
                })

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun chooseImage(){
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
        isPdf = false
    }

    fun choosePdf(){
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_CODE_PDF_PICKER)
        isPdf = true
    }

    fun uploadFile(){
        if (selectedFile == null){
            Toast.makeText(this, "Select a File first", Toast.LENGTH_SHORT).show()
            return
        }

        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFile!!, "r") ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedFile!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)

        presenter?.uploadFile(
                MultipartBody.Part.createFormData("picture", file.name, body))
    }

    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedFile = data?.data
                    btnChooseFile.setImageURI(selectedFile)
                }
                REQUEST_CODE_PDF_PICKER -> {
                    selectedFile = data?.data
                    btnChooseFile.visibility = View.GONE
                    pdfPreview.visibility = View.VISIBLE
                    pdfPreview.fromUri(selectedFile).load()
                }
            }
        }
    }

    override fun uploadSuccess(file: String?) {
        progressBar.progress = 100
        Toast.makeText(this, "upload file berhasil", Toast.LENGTH_SHORT).show()
        if (isPdf == true){
            Toast.makeText(this, "umasuk siniii", Toast.LENGTH_SHORT).show()
            filePreview.visibility = View.GONE
            webView.visibility = View.VISIBLE

            webView.settings.javaScriptEnabled = true
            webView.settings.pluginState = PluginState.ON
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=$file")

        }else{
            filePreview.visibility = View.VISIBLE
            webView.visibility = View.GONE

            Picasso.get()
                    .load(if (TextUtils.isEmpty(file)) null else Uri.parse(file))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .centerInside()
                    .into(filePreview)
        }
    }

    override fun uploadFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val REQUEST_CODE_IMAGE_PICKER = 100
        private const val REQUEST_CODE_PDF_PICKER = 101
    }
}