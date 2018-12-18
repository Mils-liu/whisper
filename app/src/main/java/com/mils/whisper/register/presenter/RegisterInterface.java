package com.mils.whisper.register.presenter;

import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2018/6/5.
 */

public interface RegisterInterface {

    interface View {
        void toHome();

        void registerLoading();

        void registerEnd(String msg);
    }

    interface Presenter {
        void onRegister(String username, String password,
                        String password_confirm, Integer sex);
    }

    interface Interactor {
        void doRegister(String username, String password,
                        Integer sex, RegisterCallback callback);

        interface RegisterCallback {
            void onSuccess();

            void onFailure();
        }
    }

}
