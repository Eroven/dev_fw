package me.zhaotb.app.api.station;

import java.nio.ByteBuffer;

/**
 * 基站之间互投的消息
 * @author zhaotangbo
 * @date 2019/3/4
 */
public abstract class Msg {

    //启动指令
    private static final int CMD_SET_UP = 1 << 5;


    /**
     *
     * @return 消息体
     */
    public abstract byte[] content();

    /**
     *
     * @return 消息大小
     */
    public abstract int size();

    /**
     * 查询类型
     */
    public enum QueryType {

    }

    /**
     * 查询功能的消息
     */
    public static class QueryMsg extends Msg {

        private QueryType type;

        private String[] args;

        public QueryMsg set(QueryType type, String...args){
            this.type = type;
            this.args = args;
            return this;
        }

        @Override
        public byte[] content() {
            return new byte[0];
        }

        @Override
        public int size() {
            return 0;
        }
    }

    public enum  ExecuteType {

        SET_UP("启动程序", CMD_SET_UP);

        private String desc;
        private int cmd;

        ExecuteType(String desc, int cmd) {
            this.desc = desc;
            this.cmd = cmd;
        }
    }

    /**
     * 执行功能的消息
     */
    public static class ExecuteMsg extends Msg {

        private ExecuteType type;

        private String[] args;

        public ExecuteMsg set(ExecuteType type, String...args){
            this.type = type;
            this.args = args;
            return this;
        }

        @Override
        public byte[] content() {
            return new byte[0];
        }

        @Override
        public int size() {
            return 0;
        }
    }


    public static class TickMsg extends Msg {

        @Override
        public byte[] content() {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(System.currentTimeMillis());
            return buffer.array();
        }

        @Override
        public int size() {
            return 8;
        }
    }

}
