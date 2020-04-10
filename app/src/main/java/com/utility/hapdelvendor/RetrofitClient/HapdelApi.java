package com.utility.hapdelvendor.RetrofitClient;

import com.utility.hapdelvendor.Model.AddressModel.AddressModel;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.ParentCategoryModel;
import com.utility.hapdelvendor.Model.CouponModel.ApplyCouponModel.ApplyCouponModel;
import com.utility.hapdelvendor.Model.CouponModel.CouponModel;
import com.utility.hapdelvendor.Model.DeliveryModel.DeliveryFareModel;
import com.utility.hapdelvendor.Model.DiscountModel.DiscountModel;
import com.utility.hapdelvendor.Model.FilterModel.WeightListModel.WeightListModel;
import com.utility.hapdelvendor.Model.LoginModel.UserModel;
import com.utility.hapdelvendor.Model.NotificationModel.NotificationModel;
import com.utility.hapdelvendor.Model.PackageCategoryModel.PackageContentModel.PackageContentModel;
import com.utility.hapdelvendor.Model.PackageCategoryModel.WeightTypeModel.WeightTypeModel;
import com.utility.hapdelvendor.Model.ProducModel.ProducModel;
import com.utility.hapdelvendor.Model.ProfileModel.UserDetailModel;
import com.utility.hapdelvendor.Model.RecentOrderModel.RecentOrderModel;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Model.SearchModel.SearchResultModel;
import com.utility.hapdelvendor.Model.SlotModel.DeliverySlotModel;
import com.utility.hapdelvendor.Model.TransactionModel.TransactionModel;
import com.utility.hapdelvendor.Model.UploadDocModel.UploadDocModel;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.OrderDetailModel;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderModel.OrderModel;
import com.utility.hapdelvendor.Model.VendorModel.VendorModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HapdelApi {
    @FormUrlEncoded
    @POST("categories/get_parent_categories")
    Call<ParentCategoryModel> fetchParentCategory(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("categories/get_listed_categories")
    Call<ParentCategoryModel> fetchSubCategory(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("parent_id") String parent_id
    );

    @FormUrlEncoded
    @POST("products/list")
    Call<ProducModel> fetchProducts(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("category_id") String category_id,
            @Field("search") String keyword,
            @Field("page") String page
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseModel> loginUser(
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("users/user/registration")
    Call<ResponseModel> signUpUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("password") String pswsd
    );

    @FormUrlEncoded
    @POST("user/verify_otp")
    Call<UserModel> verifyOtp(
            @Field("otp") String otp,
            @Field("mobile") String mobile

    );

    @FormUrlEncoded
    @POST("users/cart/add_item")
    Call<ResponseModel> updateCart(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("product_id") String pid,
            @Field("quantity") int newValue,
            @Field("seller_id") String seller_id);


    @FormUrlEncoded
    @POST("user/logout")
    Call<ResponseModel> logoutUser(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/cart/get_delivery_time")
    Call<DeliverySlotModel> fetchDeliverySlot(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/cart/update_delivery_slot")
    Call<ResponseModel> selectSlot(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("delivery_slot_id") String slot_id
    );

    @FormUrlEncoded
    @POST("users/cart/fetch_coupons")
    Call<CouponModel> fetchCoupons(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/cart/add_coupon")
    Call<ApplyCouponModel> applyCoupon(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("coupon") String coupon
    );

    @FormUrlEncoded
    @POST("users/cart/remove_coupon")
    Call<ResponseModel> cancelCoupon(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/user/save_address")
    Call<ResponseModel> saveAddress(
            @Field("user_id") String id,
            @Field("access_token") String accessToken,
            @Field("name") String full_name,
            @Field("mobile") String contact,
            @Field("house_no") String house,
            @Field("apartment_name") String apartment,
            @Field("street_address") String street,
            @Field("address") String area,
            @Field("landmark") String landmark,
            @Field("pincode") String pin,
            @Field("city") String city,
            @Field("state") String state,
            @Field("lat") String lat,
            @Field("long") String lng,
            @Field("default") String def,
            @Field("address_type") String address_type,
            @Field("address_id") String address_id
    );

    @FormUrlEncoded
    @POST("users/user/fetch_all_addresses")
    Call<AddressModel> fetchAddress(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/order/create_order")
    Call<ResponseModel> createOrder(
            @Field("user_id") String id,
            @Field("access_token") String accessToken,
            @Field("order_id") String orderid,
            @Field("gateway") String gateway,
            @Field("address_id") String addressId,
            @Field("service_time_slot") String service_timing);


    @FormUrlEncoded
    @POST("users/user/remove_address")
    Call<ResponseModel> deleteAddress(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("address_id") String address_id
    );


    @FormUrlEncoded
    @POST("users/products/filter_products")
    Call<ProducModel> applyFilter(
            @Field("category_id") String category_id,
            @Field("weight[]") List<String> weightFilter,
            @Field("brand[]") List<String> brandFilter,
            @Field("latitude") String lat,
            @Field("longitude") String lng

    );

    @FormUrlEncoded
    @POST("users/products/get_filter_weight_list")
    Call<WeightListModel> fetchWeightFilters(
            @Field("category_id") String category_id

    );



    @FormUrlEncoded
    @POST("users/cart/remove_cart")
    Call<ResponseModel> removeCart(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );




    @FormUrlEncoded
    @POST("products/fetch_category_products")
    Call<SearchResultModel> searchItem(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("category_id") String categoryId,
            @Field("query") String keyword
    );

    @FormUrlEncoded
    @POST("user/update_profile")
    Call<ResponseModel> updateProfile(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("display_name") String name,
            @Field("store_name") String store_name,
            @Field("store_addres") String store_addres
    );


    @FormUrlEncoded
    @POST("order/order_details")
    Call<OrderDetailModel> fetchVendorOrderDetails(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("order_id") String order_id
    );

    @FormUrlEncoded
    @POST("order/all_orders")
    Call<OrderModel> fetchVendorOrder(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("page") String page
    );


    @FormUrlEncoded
    @POST("order/order_status")
    Call<ResponseModel> updateOrder(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("order_id") String item_id,
            @Field("status") String status
    );



    //VendorList
    @FormUrlEncoded
    @POST("users/user/get_nearby_sellers")
    Call<VendorModel> fetch_all_vendors(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("lat") String latitude,
            @Field("long") String longitude
    );

    @FormUrlEncoded
    @POST("users/user/get_favorite")
    Call<VendorModel> fetch_fav_vendors(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("lat") String latitude,
            @Field("long") String longitude
    );

    @FormUrlEncoded
    @POST("users/products/get_products_by_seller")
    Call<ProducModel> fetchProductsBySeller(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("seller_id") String seller_id,
            @Field("page") String page
    );

    @FormUrlEncoded
    @POST("users/delivery/fetch_delivery_types")
    Call<PackageContentModel> fetchPackageContents(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/delivery/fetch_weight_type")
    Call<WeightTypeModel> fetchWeightTypes(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/delivery/calculate_fare_price")
    Call<DeliveryFareModel> deliveryFare(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("weight_id") String weight_id,
            @Field("pickup_address_id") String pickup_address_id,
            @Field("drop_address_id") String drop_address_id
    );

    @FormUrlEncoded
    @POST("users/delivery/create_delivery_order")
    Call<ResponseModel> createPackageDeliveryOrder(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("weight_id") String weight_id,
            @Field("pickup_address_id") String pickup_address_id,
            @Field("drop_address_id") String drop_address_id,
            @Field("order_type") String payment_mode,
            @Field("delivery_type") String courier_type,
            @Field("pickup_datetime") String date_time,
            @Field("instructions") String instructions
    );

    @FormUrlEncoded
    @POST("users/user/save_favorite")
    Call<ResponseModel> saveAsFavorite(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("seller_id") String seller_id,
            @Field("action") String action
    );

    @FormUrlEncoded
    @POST("users/user/is_service_available")
    Call<ResponseModel> checkServiceAvailibility(
            @Field("latitude") String lat,
            @Field("longitude") String lng
    );

    @FormUrlEncoded
    @POST("users/order/get_delivery_charges")
    Call<ResponseModel> fetchDeliveryCharges(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("address_id") String address_id
    );

    @FormUrlEncoded
    @POST("order/recent_orders")
    Call<RecentOrderModel> fetchRecentOrders(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("page") String page
            );

    @FormUrlEncoded
    @POST("user/get_profile")
    Call<UserDetailModel> fetchUserDetails(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );


    @FormUrlEncoded
    @POST("notifications/get_notifications")
    Call<NotificationModel> getNotifications(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("page") String page
    );

    @FormUrlEncoded
    @POST("products/add_product")
    Call<ResponseModel> addProduct(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("category_id") String catId,
            @Field("product_id") String current_product_id,
            @Field("stock") String stock,
            @Field("price") String price
    );

    @FormUrlEncoded
    @POST("products/remove_product")
    Call<ResponseModel> deleteProduct(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("category_id") String catId,
            @Field("product_id") String current_product_id
    );

    @FormUrlEncoded
    @POST("categories/add_discount")
    Call<ResponseModel> addDiscount(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("category_id") String category_id,
            @Field("discount") String discount,
            @Field("start_date") String start_date,
            @Field("expiry_date") String expiry_date,
            @Field("max_discount_amount") String max_discount_amount,
            @Field("minimum_order_amount") String minimum_order_amount,
            @Field("discount_id") String discount_id
        );

    @FormUrlEncoded
    @POST("categories/get_discounts")
    Call<DiscountModel> getDiscount(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("category_id") String category_id
    );

    @FormUrlEncoded
    @POST("user/get_transactions")
    Call<TransactionModel> fetchTransactions(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("page") String filter
    );


    @FormUrlEncoded
    @POST("order/get_order_by_txn_id")
    Call<OrderDetailModel> fetchUserOrderDetails(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("txn_id") String txn_id
    );


    @Multipart
    @POST("user/register")
    Call<ResponseModel> registration(
            @Part MultipartBody.Part body1,
            @Part MultipartBody.Part body2,
            @Part MultipartBody.Part body3,
            @Part MultipartBody.Part body4,
            @Part MultipartBody.Part body5,
            @Part MultipartBody.Part body6,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("password") RequestBody pass,
            @Part("store_name") RequestBody store_name,
            @Part("store_address") RequestBody store_add
    );


    @FormUrlEncoded
    @POST("user/get_uploaded_documents")
    Call<UploadDocModel> fetchDocs(
            @Field("user_id") String userId,
            @Field("access_token") String access_token
    );

    @Multipart
    @POST("user/upload_documents")
    Call<ResponseModel> uploadDoc(
            @Part("user_id") RequestBody userId,
            @Part MultipartBody.Part body1
    );

    @FormUrlEncoded
    @POST("notifications/update_notification_token")
    Call<ResponseModel> sendNotificationToken(
            @Field("user_id") String userId,
            @Field("access_token") String access_token,
            @Field("token") String token
    );
}


