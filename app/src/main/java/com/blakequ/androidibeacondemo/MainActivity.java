package com.blakequ.androidibeacondemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blakequ.androidibeacondemo.ble.BluetoothUtils;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    private final String TAG = "MainActivity";
    /** 设置兴趣UUID*/
    public static final String FILTER_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
//    private BeaconManager beaconManager;
    private BluetoothUtils bleUtils;
    private BeaconAdapter mAdapter;

    private TextView mTvMoniter;
    private TextView mTvRange;
    private ListView mListView;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0){
                mTvMoniter.setText((String)msg.obj);
            }else{
                Beacon beacon = (Beacon) msg.obj;
                mAdapter.add(beacon);
                mTvRange.setText(beacon.getBluetoothAddress()+" "+beacon.getBluetoothName()+" "+beacon.getDistance());
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mAdapter.clear();
            }
        });
        bleUtils = BluetoothUtils.getInstance();
        requestLocationPermission();
//        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//        beaconManager.getBeaconParsers().clear();
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
//        beaconManager.bind(this);
        if (!BluetoothUtils.isBluetoothLeSupported(this) || bleUtils.isBluetoothOn()){
            bleUtils.askUserToEnableBluetoothIfNeeded(this);
        }

        mTvMoniter = (TextView) findViewById(R.id.beacon_tv_moniter);
        mTvRange = (TextView) findViewById(R.id.beacon_tv_range);
        mListView = (ListView) findViewById(R.id.beacon_list);
        mAdapter = new BeaconAdapter(this);
        mListView.setAdapter(mAdapter);
        ((MainApplication)getApplicationContext()).setMonitoringActivity(this);
    }

    public void logToDisplay(String text){
        Message msg = new Message();
        msg.what = 0;
        msg.obj = text;
        handler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
//        beaconManager.setMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = "didEnterRegion:"+region.toString();
//                handler.sendMessage(msg);
//                Log.i(TAG, "I just saw an beacon for the first time!"+region.getBluetoothAddress());
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = "didExitRegion:"+region.toString();
//                handler.sendMessage(msg);
//                Log.i(TAG, "I no longer see an beacon "+region.getBluetoothAddress());
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int state, Region region) {
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = "didDetermineStateForRegion state:"+(state==0?"OUTSIDE":"INSIDE")+" "+region.toString();
//                handler.sendMessage(msg);
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
//            }
//        });
//        beaconManager.setRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                if (beacons.size() > 0) {
//                    Beacon beacon = beacons.iterator().next();
//                    Message msg = new Message();
//                    msg.what = 1;
//                    msg.obj = beacon;
//                    handler.sendMessage(msg);
//                    Log.i(TAG, "The first beacon I see is about size:"+beacons.size()+", distance:" + beacon.getDistance() + " meters away. mac:"+region.getBluetoothAddress()
//                            +", address:"+beacon.getBluetoothAddress());
//                }
//            }
//        });

//        try {
//            beaconManager.startRangingBeaconsInRegion(new Region(FILTER_UUID, null, null, null));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            beaconManager.startMonitoringBeaconsInRegion(new Region(FILTER_UUID, null, null, null));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }



    private void requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            Toast.makeText(this, "还没有打开蓝牙设备", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, TestActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
