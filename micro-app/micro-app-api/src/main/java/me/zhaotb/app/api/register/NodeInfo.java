package me.zhaotb.app.api.register;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zhaotangbo
 * @date 2019/3/5
 */
@Setter
@Getter
@NoArgsConstructor
public class NodeInfo {

    private Address tickAddr;

    private Address ctrlAddr;

    private ProgramInfo programInfo;

    public NodeInfo(Address tickAddr, Address ctrlAddr) {
        this.tickAddr = tickAddr;
        this.ctrlAddr = ctrlAddr;
    }

    public void addPid(int pid){
        if (programInfo == null){
            synchronized (this){
                if (programInfo == null){
                    programInfo = new ProgramInfo();
                }
            }
        }
        programInfo.getPidSet().add(pid);
    }

    public void remove(int pid){
        if (programInfo == null){
            return;
        }
        programInfo.getPidSet().remove(pid);
    }

}
