package me.zhaotb.app.api.register;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * @author zhaotangbo
 * @date 2019/3/5
 */
@Setter
@Getter
public class ProgramInfo {

    private String programName;

    private String cmd;

    /**
     * 进程id集合
     */
    private HashSet<Integer> pidSet = new HashSet<>();
}
