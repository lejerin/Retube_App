package com.example.shopping.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopping.R
import com.example.shopping.data.model.ProductRequest
import com.example.shopping.data.model.ProductResponse
import com.example.shopping.data.network.ShopApi
import com.example.shopping.data.repository.ApiRepository
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var factory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    val REQUEST_IMAGE_CAPTURE = 10
    lateinit var currentPhotoPath : String //문자열 형태의 사진 경로값 (초기값을 null로 시작하고 싶을 때 - lateinti var)


    private val products: MutableList<ProductResponse> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        rc_home.layoutManager = LinearLayoutManager(requireContext())
        rc_home.setHasFixedSize(true)
        rc_home.adapter = HomeAdapter(products)

        val api = ShopApi()
        val repository = ApiRepository(api)
        factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        //viewModel.getProductList()
        viewModel.products.observe(viewLifecycleOwner, Observer { list ->

            products.clear()
            products.addAll(list)
            rc_home.adapter!!.notifyDataSetChanged()

        })


        btn_add_product.setOnClickListener {

            setPermission()

        }


    }

    private fun testRetrofit(path : String) {

        val product = ProductRequest("name", "detail" , 5000 ,
            3 , "category" , "category_minor",
            3 , "eunjin" )


        val product_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "name")
        val product_detail: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "product_detail")
        val product_price: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "3")
        val product_stock: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "3")
        val product_major_category: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "product_major_category")
        val product_minor_category: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "product_minor_category")
        val product_merchandiser: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"), "22cun2")

        //creating a file
        val file = File(path)
        var fileName = "send_image_test"
        fileName += ".png"


        val requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"),file)



        val image = MultipartBody.Part.createFormData("product_image",fileName,requestBody)


        System.out.println("이미지" + image)

        viewModel.uploadProduct(image,  product_name, product_detail,product_price,product_stock,
            product_major_category,product_minor_category, product_merchandiser)

    }


    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {//설정해 놓은 위험권한(카메라 접근 등)이 허용된 경우 이곳을 실행
                takeCapture()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {//설정해 놓은 위험권한이 거부된 경우 이곳을 실행
                Toast.makeText(context,"요청하신 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(context)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다.앱을 사용하시려면 [앱 설정]-[권한] 항목에서 권한을 허용해주세요.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
            .check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val bitmap : Bitmap
            val file = File(currentPhotoPath)
            bitmap = if(Build.VERSION.SDK_INT < 28){//안드로이드 9.0 보다 버전이 낮을 경우
                MediaStore.Images.Media.getBitmap(context!!.contentResolver,Uri.fromFile(file))
                //  img_photo.setImageBitmap(bitmap)
            }else{//안드로이드 9.0 보다 버전이 높을 경우
                val decode = ImageDecoder.createSource(
                    this.context!!.contentResolver,
                    Uri.fromFile(file)
                )
                ImageDecoder.decodeBitmap(decode)
                //  img_photo.setImageBitmap(bitmap)
            }

            testRetrofit( file.toString())


        }




    }

    //기본 카메라 앱을 사용해서 사진 촬영
    private fun takeCapture() {
        //기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                val photoFile : File? = try{
                    createImageFile()
                }catch (e:Exception){
                    null
                }
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                        context!!,
                        "com.example.shopping.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    //이미지 파일 생성
    private fun createImageFile(): File {
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPG_${timestamp}_",".jpg",storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun Uri.getPathString(context: Context): String {
        var path: String = ""

        context.contentResolver.query(
            this, arrayOf(MediaStore.Images.Media.DATA),
            null, null, null
        )?.apply {
            val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            moveToFirst()
            path = getString(columnIndex)
            close()
        }

        return path
    }


}