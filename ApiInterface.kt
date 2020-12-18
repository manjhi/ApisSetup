interface ApiInterface {
    @FormUrlEncoded
    @POST(ApiEndpoint.LOGIN)
    fun login(
        @HeaderMap header: HashMap<String, String>,
        @FieldMap params: HashMap<String, String>
    ): Call<UserEntity>
}
