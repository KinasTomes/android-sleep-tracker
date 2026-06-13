package com.uet.sleeptracker.notifications

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object FcmTokenRegistrar {
    private const val BASE_URL = "http://35.240.197.142:8000"
    private const val DEMO_USER_ID = "00000000-0000-0000-0000-000000000001"

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun register(token: String) {
        val body = """
            {
              "token": "$token",
              "platform": "android"
            }
        """.trimIndent().toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$BASE_URL/api/v1/devices/register")
            .addHeader("X-User-Id", DEMO_USER_ID)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.close()
            }
        })
    }
}
