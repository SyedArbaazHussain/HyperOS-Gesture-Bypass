package com.arbaaz.hyperos.gesture.bypass

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HyperOSGestureHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        
        // 1. Hook System Framework (android)
        if (lpparam.packageName == "android") {
            val classLoader = lpparam.classLoader

            // Disable Xiaomi's hardware-based button enforcement
            XposedHelpers.findAndHookMethod(
                "miui.util.HardwareInfo",
                classLoader,
                "isSupportForceHidingNavbar",
                XC_MethodReplacement.returnConstant(true)
            )

            // Spoof the "Is Gestures Enabled" check for AOSP compatibility
            // This prevents the system from thinking it's in a 'broken' state
            XposedHelpers.findAndHookMethod(
                "android.provider.MiuiSettings\$System",
                classLoader,
                "getBoolean",
                android.content.ContentResolver::class.java,
                String::class.java,
                Boolean::class.java,
                object : de.robv.android.xposed.XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val name = param.args[1] as String
                        if (name == "force_fsg_nav_bar") {
                            param.result = true
                        }
                    }
                }
            )
        }

        // 2. Hook System UI - Force AOSP Gesture Mode
        if (lpparam.packageName == "com.android.systemui") {
            val classLoader = lpparam.classLoader

            // Force the navigation mode to '2' (which is the AOSP Gestural constant)
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.navigationbar.NavigationModeController",
                classLoader,
                "getCurrentInteractionMode",
                XC_MethodReplacement.returnConstant(2) 
            )

            // Kill the MIUI-specific back gesture handler so it doesn't conflict with AOSP
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.navigationbar.gestural.MiuiEdgeBackGestureHandler",
                classLoader,
                "isGestureMode",
                XC_MethodReplacement.returnConstant(false)
            )
        }
    }
}