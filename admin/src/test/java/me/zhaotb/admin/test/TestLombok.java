package me.zhaotb.admin.test;


import me.zhaotb.admin.api.LombokDto;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author zhaotangbo
 * @date 2021/1/18
 */
public class TestLombok {

    @Test
    public void testLombokUser() {
        LombokDto dto = new LombokDto(1, Arrays.asList("1", "2"));
        LombokDto b = dto.withAge(2).withHobbies(Collections.singletonList("A"));
        System.out.println(dto);
        System.out.println(b);
        try {
            System.out.println(b.doEncode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
