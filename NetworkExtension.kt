
fun <T> Call<T>.enqueueCall(callback: CallBackKt<T>.() -> Unit) {
    if (isNetworkAvailable()) {
        val callBackKt = CallBackKt<T>()
        callback.invoke(callBackKt)
        this.enqueue(callBackKt)
    } else {
        disposeProgress()
        "Check Your Internet Connection".toast()
    }
}

fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        App.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo
        .isConnected
}

class CallBackKt<T> : Callback<T> {

    var onResponse: ((ApiResponse<T>) -> Unit)? = null
    // var onFailure: ((t: ApiResponse<Throwable>) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        var response = ApiResponse(
            status = false,
            message = t.message!!,
            data = t as T
        )
        onResponse?.invoke(response)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            var response1 = ApiResponse(
                status = response.isSuccessful,
                message = if (response.errorBody() == null) response.message() else response.errorBody()
                    ?.getError()!!,
                data = response.body()
            )
            onResponse?.invoke(response1)
        } else {

            if (response.code() == 401) {

                var response1 = ApiResponse<T>(
                    status = false,
                    message = if (response.errorBody() == null) response.message() else response.errorBody()
                        ?.getError()!!,
                    data = null,
                    statusCode = response.code()
                )
                onResponse?.invoke(response1)
            } else {

                var response1 = ApiResponse<T>(
                    status = false,
                    message = if (response.errorBody() == null) response.message() else response.errorBody()
                        ?.getError()!!,
                    data = null
                )
                onResponse?.invoke(response1)
            }
        }
    }

}

fun ResponseBody.getError(): String? {
    return try {
        val jObjError = JSONObject(this.string())
        jObjError["Message"].toString()

    } catch (e: Exception) {
        e.message
    }.toString()
}
