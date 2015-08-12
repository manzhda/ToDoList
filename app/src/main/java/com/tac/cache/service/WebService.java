package com.tac.cache.service;

import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.tac.cache.ErrorUtils;
import com.tac.cache.command.ServiceCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class WebService extends Service {

    private static final String TAG = WebService.class.getSimpleName();

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    protected static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private final BlockingQueue<Runnable> threadQueue;

    private Map<String, ServiceCommand> serviceCommandMap = new HashMap<String, ServiceCommand>();
    private ThreadPoolExecutor threadPool;

    public WebService() {
        threadQueue = new LinkedBlockingQueue<Runnable>();
        serviceCommandMap = getCommandHelper().getServiceCommandMap(this);
        initThreads();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action;
        if (intent != null && (action = intent.getAction()) != null) {
            Log.d(TAG, "service started with resultAction = " + action);
            ServiceCommand command = serviceCommandMap.get(action);
            if (command != null) {
                startAsync(command, intent);
            }
        }
        return START_NOT_STICKY;
    }

    protected abstract CommandHelper getCommandHelper();

    private void initThreads() {
        threadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT, threadQueue);
        threadPool.allowCoreThreadTimeOut(true);
    }

    private void startAsync(final ServiceCommand command, final Intent intent) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "executing with resultAction = " + intent.getAction());
                try {
                    command.execute(intent.getExtras());
                } catch (Exception e) {
                    ErrorUtils.logError(e);
                }
            }
        });
    }
}
