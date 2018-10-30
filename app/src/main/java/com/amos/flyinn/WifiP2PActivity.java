package com.amos.flyinn;

import android.app.ListActivity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.amos.flyinn.wifimanager.WifiReceiverP2P;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WifiP2PActivity extends ListActivity {

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private boolean enableWifi;
    private List<String> nameOfPeers = new ArrayList<String>();
    private List<WifiP2pDevice> listOfPeers = new ArrayList<WifiP2pDevice>();
    private WifiReceiverP2P receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,this.nameOfPeers);
        setListAdapter(adapter);

    }

    public void setEnableWifi(boolean enableWifi) {
        this.enableWifi = enableWifi;
    }

    public void setListOfPeers(List<WifiP2pDevice> listOfPeers) {
        this.listOfPeers = listOfPeers;
        List<String> nameOfDevices = this.getNameOfPeerDevices(listOfPeers);
        Log.d("WifiP2PActivity","List of peers : " + nameOfDevices);
        Log.d("WifiP2PActivity","Devices list : " + listOfPeers);
        this.nameOfPeers.clear();
        this.nameOfPeers.addAll(nameOfDevices);
        ((ArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
    }

    private List<String> getNameOfPeerDevices(List<WifiP2pDevice> devices) {
        List<String> nameOfDevices = new ArrayList<>();
        for (WifiP2pDevice device : devices) {
            nameOfDevices.add(device.deviceName);
        }

        return nameOfDevices;

    }



    @Override
    public void onResume() {
        super.onResume();
        receiver = new WifiReceiverP2P(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}