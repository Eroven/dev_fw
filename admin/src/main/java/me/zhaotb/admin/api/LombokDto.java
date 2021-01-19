package me.zhaotb.admin.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.net.URLEncoder;
import java.util.List;

/**
 * @author zhaotangbo
 * @date 2021/1/18
 */
@Wither
@AllArgsConstructor
@ToString
public class LombokDto {

    private Integer age;

    private List<String> hobbies;

    @SneakyThrows
    public String doEncode() {
        return URLEncoder.encode(toString(), "UTF-8");
    }
}
