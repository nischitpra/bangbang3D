package com.nhuchhe.bangbang.network;

import com.nhuchhe.bangbang.inputController.adapter.NetworkControllerAdapter;

import java.util.HashMap;

public class NetworkListener extends NetworkWire {

    HashMap<String, NetworkControllerAdapter> adapterMap = new HashMap<>();

    @Override
    public void init() {
        super.init();
    }

    public void addAdapter(String id, NetworkControllerAdapter adapter) {
        adapterMap.put(id, adapter);
    }
}
