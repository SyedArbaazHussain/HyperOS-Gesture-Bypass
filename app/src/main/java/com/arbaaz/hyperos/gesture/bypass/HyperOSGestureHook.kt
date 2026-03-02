package com.arbaaz.hyperos.gesture.bypass

import android.os.Build
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HyperOSGestureHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        
        // 1. Hook System Framework (The Brain)
        if (lpparam.packageName == "android") {
            val classLoader = lpparam.classLoader

            // Bypass the "3rd party launcher" check
            try {
                XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.MiuiGesturePolicy",
                    classLoader,
                    "isDefaultLauncherSupportGesture",
                    XC_MethodReplacement.returnConstant(true)
                )
            } catch (t: Throwable) {
                // Compatibility for different HyperOS versions
                try {
                    XposedHelpers.findAndHookMethod(
                        "com.android.server.policy.MiuiShortcutPolicy",
                        classLoader,
                        "isGestureModeWithThirdPartyLauncher",
                        XC_MethodReplacement.returnConstant(true)
                    )
                } catch (e: Throwable) {}
            }

            // Force Hardware Support Flag
            XposedHelpers.findAndHookMethod(
                "miui.util.HardwareInfo",
                classLoader,
                "isSupportForceHidingNavbar",
                XC_MethodReplacement.returnConstant(true)
            )

            // INTERPRET ADB COMMANDS PROGRAMMATICALLY
            // We hook the SystemServer ready state to trigger the gesture overlay
            XposedHelpers.findAndHookMethod(
                "com.android.server.SystemServer",
                classLoader,
                "startOtherServices",
                object : de.robv.android.xposed.XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            // This replaces the manual 'adb shell settings put...' command
                            Runtime.getRuntime().exec("settings put global force_fsg_nav_bar 1")
                            // This replaces the manual 'adb shell cmd overlay enable...' command
                            Runtime.getRuntime().exec("cmd overlay enable com.android.internal.systemui.navbar.gestural")
                        } catch (e: Exception) {
                            XposedBridge.log("HyperOS-Bypass: Failed to auto-trigger gesture overlay")
                        }
                    }
                }
            )
        }

        // 2. Hook System UI (The Visuals)
        if (lpparam.packageName == "com.android.systemui") {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.navigationbar.gestural.MiuiEdgeBackGestureHandler",
                lpparam.classLoader,
                "isGestureMode",
                XC_MethodReplacement.returnConstant(true)
            )
        }
    }
}