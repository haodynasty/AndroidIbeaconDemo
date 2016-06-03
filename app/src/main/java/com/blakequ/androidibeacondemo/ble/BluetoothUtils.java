package com.blakequ.androidibeacondemo.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.blakequ.androidibeacondemo.MainApplication;


/**
 * the utils of bluetooth
 *
 * <li>
 * <li>1.check phone is supports BLE features{@link #isBluetoothLeSupported(Context)}
 * <li>2.the bluetooth is open or enable{@link #isBluetoothOn()}
 * <li>3.if the bluetooth is closed you can using this method open system setting to open bluetooth{@link #askUserToEnableBluetoothIfNeeded(Activity)}??{@link #openBlueToothSetting(Activity)}
 */
public final class BluetoothUtils {
    private static String TAG = "BluetoothUtils";
    public final static int REQUEST_ENABLE_BT = 2001;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothManager mBluetoothManager = null;
    private static BluetoothUtils mBluetoothUtils;

    public static synchronized BluetoothUtils getInstance(){
        if (mBluetoothUtils == null){
            mBluetoothUtils = new BluetoothUtils();
        }
        return mBluetoothUtils;
    }

    private BluetoothUtils() {
        mBluetoothManager = (BluetoothManager) MainApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager == null) {
            Log.e(TAG, "Unable to initialize BluetoothManager.");
            return;
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    /**
     * get bluetooth manager
     * @return
     */
    public BluetoothManager getBluetoothManager() {
        return mBluetoothManager;
    }

    /**
     * get adapter
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }


    /**
     * the bluetooth is enable
     * @return
     */
    public boolean isBluetoothOn() {
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            return mBluetoothAdapter.isEnabled();
        }
    }

    /**
     * open system setting to open bluetooth
     * <p>Notification of the result of this activity is posted using the
     * {@link Activity#onActivityResult} callback. The
     * <code>resultCode</code>
     * will be {@link Activity#RESULT_OK} if Bluetooth has been
     * turned on or {@link Activity#RESULT_CANCELED} if the user
     * has rejected the request or an error has occurred.
     */
    public void askUserToEnableBluetoothIfNeeded(Activity activity) {
        if (isBluetoothLeSupported(activity) && (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())) {
            final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * you phone is support bluetooth feature
     * @return
     */
    public static boolean isBluetoothLeSupported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * open system setting to open bluetooth
     * <p>Notification of the result of this activity is posted using the
     * {@link Activity#onActivityResult} callback. The
     * <code>resultCode</code>
     * will be {@link Activity#RESULT_OK} if Bluetooth has been
     * turned on or {@link Activity#RESULT_CANCELED} if the user
     * has rejected the request or an error has occurred.
     * @param mActivity
     */
    public static void openBlueToothSetting(Activity mActivity){
        final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
}
