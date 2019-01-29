package me.zhaotb.dt.sdk.api;

import java.util.List;

public class DispatchRule {

    private DispatchType type;

    private List<Object> contents;

    public DispatchType getType() {
        return type;
    }

    public void setType(DispatchType type) {
        this.type = type;
    }

    public List<Object> getContents() {
        return contents;
    }

    public void setContents(List<Object> contents) {
        this.contents = contents;
    }
}
