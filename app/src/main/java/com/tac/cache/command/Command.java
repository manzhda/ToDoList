package com.tac.cache.command;

import android.os.Bundle;

public interface Command {

    void execute(Bundle bundle) throws Exception;
}