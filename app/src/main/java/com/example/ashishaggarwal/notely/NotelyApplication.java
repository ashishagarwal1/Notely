package com.example.ashishaggarwal.notely;

import android.app.Application;
import android.content.Context;

import com.example.ashishaggarwal.notely.injection.component.ApplicationComponent;
import com.example.ashishaggarwal.notely.injection.component.DaggerApplicationComponent;
import com.example.ashishaggarwal.notely.injection.module.ApplicationModule;

public class NotelyApplication extends Application {

    ApplicationComponent mApplicationComponent;

    private static NotelyApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    public static NotelyApplication getInstance() {
        return _instance;
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
