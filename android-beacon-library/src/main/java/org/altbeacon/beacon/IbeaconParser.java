package org.altbeacon.beacon;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;

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
 * date     : 2016/6/3 9:51 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 *  A specific beacon parser designed to parse only IBeacons from raw BLE packets detected by
 * Android.  By default, this is the only <code>BeaconParser</code> that is used by the library.
 * Additional <code>IbeaconParser</code> instances can get created and registered with the library.
 * {@link BeaconParser See BeaconParser for more information.}
 */
public class IbeaconParser extends BeaconParser {

    /**
     * Constructs an AltBeacon Parser and sets its layout
     */
    public IbeaconParser() {
        super();
        // Radius networks and other manufacturers seen in AltBeacons
        // Note: Other manufacturer codes that have been seen in the wild with AltBeacons are:
        // 0x004c, 0x00e0
        // We are not adding these here because there is no indication they are widely used
        // for production purposes.  We need to keep the hardware assist list short in order to
        // save slots.  If you are a manufacturer of AltBeacons and want you company code added to
        // this list, please open an issue on the Github project for this library.  If a beacon
        // manufacturer code not in this list is used for AltBeacons, phones using Andoroid 5.x+
        // detection APIs will not be able to detect the beacon in the background.
        mHardwareAssistManufacturers = new int[]{0x004c};
        this.setBeaconLayout(BeaconParser.IBEACON_LAYOUT);
//        this.mIdentifier = "altbeacon";
    }
    /**
     * Construct an AltBeacon from a Bluetooth LE packet collected by Android's Bluetooth APIs,
     * including the raw Bluetooth device info
     *
     * @param scanData The actual packet bytes
     * @param rssi The measured signal strength of the packet
     * @param device The Bluetooth device that was detected
     * @return An instance of an <code>Beacon</code>
     */
    @TargetApi(5)
    @Override
    public Beacon fromScanData(byte[] scanData, int rssi, BluetoothDevice device) {
        return fromScanData(scanData, rssi, device, new AltBeacon());
    }

}
