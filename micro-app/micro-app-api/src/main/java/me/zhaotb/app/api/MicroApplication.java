package me.zhaotb.app.api;

import com.google.common.util.concurrent.Service;

public interface MicroApplication extends Service {

    MicroAppContext getContext();

    void setContext(MicroAppContext context);

}
