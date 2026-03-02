package com.arbaaz.hyperos.gesture.bypass

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HyperOSGestureHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        
        if (lpparam.packageName == "android") {
            val classLoader = lpparam.classLoader

            // Bypass the Policy check that forces buttons on 3rd party launchers
            try {
                XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.MiuiGesturePolicy",
                    classLoader,
                    "isDefaultLauncherSupportGesture",
                    XC_MethodReplacement.returnConstant(true)
                )
                
                // Prevent auto-reset when launcher change is detected
                XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.MiuiGesturePolicy",
                    classLoader,
                    "keepGestureNavAfterLauncherChange",
                    XC_MethodReplacement.returnConstant(true)
                )
            } catch (e: Throwable) {
                // Fallback for sub-versions of HyperOS
                try {
                    XposedHelpers.findAndHookMethod(
                        "com.android.server.policy.MiuiShortcutPolicy",
                        classLoader,
                        "isGestureModeWithThirdPartyLauncher",
                        XC_MethodReplacement.returnConstant(true)
                    )
                } catch (t: Throwable) {}
            }

            // Patch hardware info to allow hiding navigation bar
            XposedHelpers.findAndHookMethod(
                "miui.util.HardwareInfo",
                classLoader,
                "isSupportForceHidingNavbar",
                XC_MethodReplacement.returnConstant(true)
            )
        }

        if (lpparam.packageName == "com.android.systemui") {
            // Keep the gesture listeners alive in the UI process
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.navigationbar.gestural.MiuiEdgeBackGestureHandler",
                lpparam.classLoader,
                "isGestureMode",
                XC_MethodReplacement.returnConstant(true)
            )
        }
    }
}