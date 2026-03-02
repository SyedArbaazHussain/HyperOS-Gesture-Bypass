package com.arbaaz.hyperos.gesture.bypass

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HyperOSGestureHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        
        // Target the System Framework - This is where the 'Rules' are kept
        if (lpparam.packageName == "android") {
            val classLoader = lpparam.classLoader

            // 1. Lie to the system about which launcher is 'Valid'
            // This prevents the automatic switch back to 3-button mode
            try {
                XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.MiuiGesturePolicy",
                    classLoader,
                    "isDefaultLauncherSupportGesture",
                    XC_MethodReplacement.returnConstant(true)
                )
                
                // New for HyperOS 3: Keep gestures alive even after launcher change detected
                XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.MiuiGesturePolicy",
                    classLoader,
                    "keepGestureNavAfterLauncherChange",
                    XC_MethodReplacement.returnConstant(true)
                )
            } catch (t: Throwable) {
                // Fallback for different HyperOS point releases
                XposedHelpers.findAndHookMethod(
                    "com.android.server.policy.MiuiShortcutPolicy",
                    classLoader,
                    "isGestureModeWithThirdPartyLauncher",
                    XC_MethodReplacement.returnConstant(true)
                )
            }

            // 2. Force Hardware Compatibility
            // Tells the OS the hardware supports hiding the bar regardless of app
            XposedHelpers.findAndHookMethod(
                "miui.util.HardwareInfo",
                classLoader,
                "isSupportForceHidingNavbar",
                XC_MethodReplacement.returnConstant(true)
            )
        }

        // 3. Target System UI - This is where the 'Visuals' are drawn
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