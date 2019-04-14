package com.github.sofaid.app.injection;

import com.github.sofaid.app.ui.dashboard.DashboardActivity;
import com.github.sofaid.app.ui.home.HomeActivity;
import com.github.sofaid.app.ui.send_ncr.SendNcrActivity;
import com.github.sofaid.app.ui.shares_activities.CreateSharesActivity;
import com.github.sofaid.app.ui.shares_activities.ShareCollectActivity;
import com.github.sofaid.app.ui.shares_activities.ShareCoordinatorActivity;
import com.github.sofaid.app.ui.signup.SignupActivity;
import com.github.sofaid.app.ui.signup_seedwords.SignupSeedWordsActivity;
import com.github.sofaid.app.ui.splash.SplashActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SignupActivity activity);

    void inject(SignupSeedWordsActivity activity);

    void inject(HomeActivity activity);

    void inject(SendNcrActivity activity);

    void inject(SplashActivity activity);

    void inject(ShareCoordinatorActivity shareCoordinatorActivity);
    void inject(DashboardActivity dashboardActivity);

    void inject(CreateSharesActivity createSharesActivity);

    void inject(ShareCollectActivity shareCollectActivity);
}
