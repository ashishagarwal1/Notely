package com.example.ashishaggarwal.notely;


import com.facebook.stetho.Stetho;

public class NotelyDbAccessApplication extends NotelyApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
