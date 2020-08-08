package test.tuto_passport;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.tuto_passport.entities.AccessToken;
import test.tuto_passport.entities.PostResponse;
import test.tuto_passport.network.ApiService;
import test.tuto_passport.network.RetrofitBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final String TAG = "MainActivity";
    ImageView display;
    private String username;
    private String myPrivateKey;


    ApiService service;
    TokenManager tokenManager;
    Call<AccessToken> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        ImageView findme = (ImageView) findViewById(R.id.findme);
        ImageView findUser = (ImageView) findViewById(R.id.finduser);
        ImageView setting = (ImageView) findViewById(R.id.setting);
        ImageView contact = (ImageView) findViewById(R.id.contact);
        ImageView review = (ImageView) findViewById(R.id.review);
        ImageView logout = (ImageView) findViewById(R.id.logout);
        TextView username = (TextView) findViewById(R.id.userName);
        String username1 = LoginActivity.getMail();
        username.setText(username1);


        findme.setOnClickListener(this);
        findUser.setOnClickListener(this);
        setting.setOnClickListener(this);
        contact.setOnClickListener(this);
        review.setOnClickListener(this);
        logout.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.findme:{

                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
                System.out.println("find me button");
            }break;
            case R.id.finduser:{
                System.out.println("find user button");
            }break;
            case R.id.setting:{
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }break;
            case R.id.review:{
                System.out.println("review button");
            }break;
            case R.id.contact:{
                System.out.println("contact button");
            }break;
            case R.id.logout:{

               call = service.logout(tokenManager.getToken().getAccessToken());

               System.out.println("=====================================");
               System.out.println(tokenManager.getToken().getAccessToken());
               call.enqueue(new Callback<AccessToken>() {
                   @Override
                   public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                       System.out.println("rani f on response :: "+response.code());
                       Log.w(TAG, "onResponse: " + response);
                        if(response.code()==402 || response.code() == 401){

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                   }

                   @Override
                   public void onFailure(Call<AccessToken> call, Throwable t) {

                   }
               });


            }break;

        }

    }


   /* @OnClick(R.id.btn_posts)
    void getPosts(){

        call = service.posts();
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){
                    title.setText(response.body().getData().get(0).getTitle());
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }


}






