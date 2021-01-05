package me.zhaotb.web.controller;

import me.zhaotb.web.dto.CommonResponse;
import me.zhaotb.web.dto.Order;
import me.zhaotb.web.dto.account.UserDto;
import me.zhaotb.web.util.CRFactory;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;

/**
 * @author zhaotangbo
 * @since 2020/12/24
 */
@RestController
@RequestMapping("/test")
public class TestDataController {

    @RequestMapping(value = "orders", method = RequestMethod.GET)
    public CommonResponse getOrderList(int size,@RequestAttribute("userDto") UserDto userDto) {
        LinkedList<Order> orders = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            orders.add(new Order(new Date(), userDto.getUserInfo().getNickName() + i, "湖北省"));
        }
        return CRFactory.okData(orders);
    }

}
