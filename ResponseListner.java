public interface ResponseListner<T> {

    void onResponse(ApiResponse<T> it);
}
