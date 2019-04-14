package com.github.sofaid.app;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import org.greenrobot.greendao.database.Database;

import java.io.File;


import com.github.sofaid.app.injection.ApplicationComponent;
import com.github.sofaid.app.injection.ApplicationModule;
import com.github.sofaid.app.injection.DaggerApplicationComponent;
import com.github.sofaid.app.models.db.DaoMaster;
import com.github.sofaid.app.models.db.DaoSession;
import timber.log.Timber;

public class NcrApplication extends MultiDexApplication {

    ApplicationComponent mApplicationComponent;
    //Green Dao Session
    DaoSession daoSession;


    private static NcrApplication instance;

    public static NcrApplication getInstance() {
        return instance;
    }

    public static NcrApplication get(Context context) {
        return (NcrApplication) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDaoSession();

        // enable timber in debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        instance = this;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().permitDiskReads()
                .permitDiskWrites().penaltyLog().build());

    }

    public ApplicationComponent applicationComponent() {
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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Timber.e("Low memory. Some functions may not work properly");
    }

    /**
     * Initialize Green Dao session
     */
    public void initializeDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "soriole-v1.2-db");
        Database db = helper.getEncryptedWritableDb("ce19958463257f0ca6b6d7ca286858d4c545c9ca");
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * Return Green Dao Session
     *
     * @return DaoSession
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            initializeDaoSession();
        }
        return daoSession;
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }
}
