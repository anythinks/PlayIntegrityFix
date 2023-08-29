package es.chiteroman.playintegrityfix;

import java.security.Provider;

public class CustomProvider extends Provider {
    public CustomProvider(Provider provider) {
        super(provider.getName(), provider.getVersion(), provider.getInfo());
        putAll(provider);
        put("KeyStore.AndroidKeyStore", CustomAndroidKeyStoreSpi.class.getName());
    }

    @Override
    public synchronized Service getService(String type, String algorithm) {
        EntryPoint.spoofDeviceProps();
        return super.getService(type, algorithm);
    }
}
