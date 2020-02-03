package com.utility.hapdelvendor.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class BasicAuthInterceptor implements Interceptor {
    private String credential;
    public BasicAuthInterceptor(String username, String password) {
        this.credential  = Credentials.basic(username, password);
    }


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticationRequest = request.newBuilder().header("Authorization", credential).build();

        return chain.proceed(authenticationRequest);
    }
}
