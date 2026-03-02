package com.arbaaz.hyperos.gesture.bypass

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HyperOSGestureHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // We need to hook the System Framework (android)
        if (lpparam.packageName == "android") {
            
            // 1. The "Is Gestures Supported" check
            XposedHelpers.findAndHookMethod(
                "miui.util.HardwareInfo", 
                lpparam.classLoader, 
                "isSupportForceHidingNavbar", 
                XC_MethodReplacement.returnConstant(true)
            )

            // 2. The "3rd Party Launcher Lock" bypass (New in HyperOS 3)
            // This class handles the policy of which launchers are "allowed"
            try {
                XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.MiuiGesturePolicy", 
                    lpparam.classLoader, 
                    "isDefaultLauncherSupportGesture", 
                    XC_MethodReplacement.returnConstant(true)
                )
            } catch (e: Throwable) {
                // Older HyperOS builds might use this path instead
                XposedHelpers.findAndHookMethod(
                    "com.android.server.policy.MiuiShortcutPolicy", 
                    lpparam.classLoader, 
                    "isGestureModeWithThirdPartyLauncher", 
                    XC_MethodReplacement.returnConstant(true)
                )
            }
        }

        // 3. System UI Hook (The visual pill/bar)
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