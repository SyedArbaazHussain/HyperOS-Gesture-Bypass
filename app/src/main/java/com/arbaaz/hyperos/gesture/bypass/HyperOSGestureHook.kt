package com.arbaaz.hyperos.gesture.bypass
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HyperOSGestureHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam.packageName != "android") return
        try {
            val dp = XposedHelpers.findClass("com.android.server.wm.DisplayPolicy", lpparam.classLoader)
            XposedHelpers.findAndHookMethod(dp, "navigationBarCanMove", object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam) = true
            })
        } catch (e: Throwable) { XposedBridge.log("Hook Failed: \") }
    }
}
