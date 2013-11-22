package com.letscode.lcg;

import java.util.Random;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.letscode.lcg.network.WebSocketClientInterface;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useWakelock = true;
        
        WebSocketClientInterface networkImpl = new WebSocketJavaClient();
        
        initialize(new LcgApp(networkImpl, LcgApp.DEFAULT_HOSTNAME, LcgApp.DEFAULT_PORT), cfg);
    }
}