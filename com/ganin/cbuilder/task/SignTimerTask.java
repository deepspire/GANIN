package com.ganin.cbuilder.task;

import com.ganin.cbuilder.util.ApkSignerUtil;
import com.naef.jnlua.LuaState;
import java.util.TimerTask;

//
public class SignTimerTask extends TimerTask {
    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
    }

    public SignTimerTask(ApkSignerUtil apkSignerUtil, LuaState luaState, String str, String str2) {
    }
}
