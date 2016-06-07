package com.blakequ.androidibeacondemo;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

/**
 * Copyright (C) BlakeQu All Rights Reserved <blakequ@gmail.com>
 * <p/>
 * Licensed under the blakequ.com License, Version 1.0 (the "License");
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * author  : quhao <blakequ@gmail.com> <br>
 * date     : 2016/6/1 11:57 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 */
public class MainApplication extends Application implements BootstrapNotifier{

    private static final String TAG = "MainApplication";
    private RegionBootstrap regionBootstrap;
    private static MainApplication INSTANCE;
    /** 设置兴趣UUID*/
    public static final String FILTER_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MainActivity monitoringActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setDebug(BuildConfig.DEBUG);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // wake up the app when any beacon is seen (you can specify specific id filers in the parameters below)
        Region region = new Region(FILTER_UUID, null, null, null);
//        beaconManager.getBeaconParsers().clear();
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        regionBootstrap = new RegionBootstrap(this, region);
        INSTANCE = this;
        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    public static MainApplication getInstance(){
        return INSTANCE;
    }

    @Override
    public void didEnterRegion(Region region) {
        Toast.makeText(this, "进入了可见范围" + region.getBluetoothAddress(), Toast.LENGTH_SHORT);
        Log.d(TAG, "did enter region.");
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
            // if you want the Activity to launch every single time beacons come into view, remove this call.
            regionBootstrap.disable();
            Intent intent = new Intent(this, MainActivity.class);
            // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
            // created when a user launches the activity manually and it gets launched from here.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display
                monitoringActivity.logToDisplay("MainApplication didEnterRegion--I see a beacon again" + region.getBluetoothAddress());
            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Sending notification.");
                sendNotification();
            }
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Toast.makeText(this, "退出了可见范围" + region.getBluetoothAddress(), Toast.LENGTH_SHORT);
        if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("MainApplication didExitRegion--I no longer see a beacon."+ region.getBluetoothAddress());
        }
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("MainApplication didDetermineStateForRegion--I have just switched from seeing/not seeing beacons: " + state+", "+ region.getBluetoothAddress());
        }
    }

    public void setMonitoringActivity(MainActivity activity) {
        this.monitoringActivity = activity;
    }

    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Reference Application")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.mipmap.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
