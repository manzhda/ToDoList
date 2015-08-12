package com.tac.cache.service;

import android.content.Context;

import com.tac.cache.command.ServiceCommand;

import java.util.Map;

/**
 * Created by tac on 12/24/14.
 */
public interface CommandHelper {
    public Map<String, ServiceCommand> getServiceCommandMap(Context context);
}
