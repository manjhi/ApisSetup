
class NetworkClient {
 internal fun getRequestHeader(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.i("Result", it)
        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .build()
            chain.proceed(request)
        }
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .readTimeout(300, TimeUnit.SECONDS)
        return httpClient.build()

    }

    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiEndpoint.BASE_URL)
            .client(getRequestHeader())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
