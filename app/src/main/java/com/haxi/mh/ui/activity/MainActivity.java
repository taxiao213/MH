package com.haxi.mh.ui.activity;

import android.view.View;
import android.widget.Button;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.network.HttpService;
import com.haxi.mh.network.request.LoginRequest;
import com.haxi.mh.utils.model.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Han on 2017/12/13
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.button)
    Button button;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void getData() {

    }


    @OnClick({R.id.button, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                new LoginRequest(mActivity).requestBack(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
                break;
            case R.id.button2:

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://www.zkhonry.com:9000/zkhonry-mobile-interface/")
//                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                HttpService service = retrofit.create(HttpService.class);
                Observable<String> login = service.login("100124", "1");
                login.subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.e(s);
                    }
                });
//                                final Call<CC> login = service.logins("100124", "1");
//                Observable.just(login).subscribeOn(Schedulers.io()).subscribe(new Consumer<Call<CC>>() {
//                    @Override
//                    public void accept(Call<CC> ccCall) throws Exception {
//                        CC body = ccCall.execute().body();
//                        LogUtils.e(body.toString());
//                    }
//                });



                break;
        }
    }
}
