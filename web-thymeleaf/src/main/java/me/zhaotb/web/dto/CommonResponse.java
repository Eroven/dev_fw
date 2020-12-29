package me.zhaotb.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaotangbo
 * @since 2020/12/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    private String status;
    private String msg;
    private Object data;

    public CommonResponse(String status) {
        this.status = status;
    }

    public CommonResponse(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
