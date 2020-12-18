

class AuthenticationRepo {

 companion object {
        val retrofitService: ApiInterface by lazy {
            NetworkClient().provideRetrofit().create(ApiInterface::class.java)
        }

        fun requests(): AuthenticationRepo {
            return AuthenticationRepo()

        }
    }
    
    
     fun login(email: String?, password: String?, listners: ResponseListner<UserEntity?>) {
        val map = HashMap<String, String>()
        map["email"] = email!!
        map["password"] = password!!
        map.putAll(basicInfo())

        retrofitService.login(getHeaders(), map).enqueueCall {
            onResponse = {
                listners.onResponse(it as ApiResponse<UserEntity?>)
            }
        }
    }
}
