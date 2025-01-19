package com.limbo.emu.main.arm;

import android.os.Bundle;

import com.max2idea.android.limbo.log.Logger;
import com.max2idea.android.limbo.main.Config;
import com.max2idea.android.limbo.main.LimboActivity;
import com.max2idea.android.limbo.links.LinksManager;
import com.max2idea.android.limbo.main.LimboApplication;

public class LimboEmuActivity extends LimboActivity {

    public void onCreate(Bundle bundle){
        LimboApplication.arch = Config.Arch.arm64;
        Config.clientClass = this.getClass();
        Config.enableKVM = true;
        Config.enableEmulatedFloppy = false;
        Config.enableEmulatedSDCard = true;
        //XXX; only for 64bit hosts, also make sure you have qemu 2.9.1 arm-softmmu and above compiled
        if(LimboApplication.isHost64Bit() && Config.enableMTTCG)
            Config.enableMTTCG = true;
        else
            Config.enableMTTCG = false;
        Config.machineFolder = Config.machineFolder + "other/arm_machines/";
        Config.osImages.put(getString(R.string.DebianArmLinux), new LinksManager.LinkInfo(getString(R.string.DebianArmLinux),
                getString(R.string.DebianArmLinuxDescr),
                "https://github.com/limboemu/limbo/wiki/Debian-ARM-Linux",
                LinksManager.LinkType.ISO));
        super.onCreate(bundle);
        //TODO: change location to something that the user will have access outside of limbo
        //  like internal storage
        Logger.setupLogFile("/limbo/limbo-arm-log.txt");
        Shizuku.addRequestPermissionResultListener((requestCode, grantResult) -> {
    if (grantResult == PackageManager.PERMISSION_GRANTED) {
        Log.d("Shizuku", "Permission granted");
    } else {
        Log.e("Shizuku", "Permission denied");
    }
});

if (Shizuku.isPreV23PermissionGranted() || Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
    Log.d("Shizuku", "Permission already granted");
} else {
    Shizuku.requestPermission(0);
}
    }

    protected void loadQEMULib(){

        try {
            System.loadLibrary("qemu-system-arm");
        } catch (Error ex) {
            System.loadLibrary("qemu-system-aarch64");
        }

    }
}
