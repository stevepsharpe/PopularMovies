package pe.shar.popularmovies.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pe.shar.popularmovies.BuildConfig;

/**
 * Created by steve on 04/03/2017.
 */

// https://futurestud.io/tutorials/android-basic-authentication-with-retrofit
// https://futurestud.io/tutorials/retrofit-2-creating-a-sustainable-android-client
public class AuthenticationInterceptor implements Interceptor {

    private static final String API_KEY_PARAM = "api_key";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl originalHttpUrl = original.url();
        HttpUrl newHttpUrl = originalHttpUrl.newBuilder()
                .setQueryParameter(API_KEY_PARAM, BuildConfig.MDB_API_KEY)
                .build();

        Request.Builder builder = original.newBuilder()
                .url(newHttpUrl);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
