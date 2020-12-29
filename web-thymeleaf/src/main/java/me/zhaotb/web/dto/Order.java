package me.zhaotb.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zhaotangbo
 * @since 2020/12/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Date date;
    private String name;
    private String address;

}
