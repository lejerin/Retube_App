package com.example.shopping.data.network

import retrofit2.Call
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            //@todo handle api exception
            System.out.println("오류" + response.code().toString())
            throw  ApiException(
                response.code().toString()
            )
        }

    }
}

class ApiException(message: String) : IOException(message)