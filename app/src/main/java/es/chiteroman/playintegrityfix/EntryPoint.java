package es.chiteroman.playintegrityfix;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.Provider;
import java.security.Security;
import java.util.Locale;

public class EntryPoint {
    private static final String TAG = "SNFix/Java";
    private static final String ANDROID_PROVIDER = "AndroidKeyStore";

    public static void init() {
        spoofDeviceProps();
        proxyKeyStore();
        proxyProvider();
    }

    public static boolean isDroidGuard() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().toLowerCase(Locale.ROOT).contains("droidguard")) {
                Log.i(TAG, "DroidGuard detected");
                return true;
            }
        }
        return false;
    }

    public static void spoofDeviceProps() {
        if (Build.HOST.equals("xiaomi.eu")) {
            Log.i(TAG, "Your ROM already spoof device props. If Play Integrity / SafetyNet still failing, contact @chiteroman on XDA");
            return;
        }
        setBuildProp("MODEL", "Pixel XL");
        setBuildProp("PRODUCT", "marlin");
        setBuildProp("DEVICE", "marlin");
        setBuildProp("FINGERPRINT", "google/marlin/marlin:7.1.2/NJH47F/4146041:user/release-keys");
        Log.i(TAG, "Spoof new device props");
    }

    private static void proxyKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_PROVIDER);
            Field field = keyStore.getClass().getDeclaredField("keyStoreSpi");

            field.setAccessible(true);
            CustomAndroidKeyStoreSpi.keyStoreSpi = (KeyStoreSpi) field.get(keyStore);
            field.setAccessible(false);

        } catch (KeyStoreException | NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, e.toString());
        }
    }

    private static void proxyProvider() {
        Provider provider = Security.getProvider(ANDROID_PROVIDER);
        Provider customProv = new CustomProvider(provider);

        Security.removeProvider(ANDROID_PROVIDER);
        Security.insertProviderAt(customProv, 1);
    }

    private static void setBuildProp(String field, String value) {
        try {
            Field f = Build.class.getDeclaredField(field);
            f.setAccessible(true);
            f.set(null, value);
            f.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, e.toString());
        }
    }
}
