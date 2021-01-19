package me.zhaotb.admin.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

/**
 * @author zhaotangbo
 * @date 2019/2/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private Long id;

    @NonNull
    private String name;

    private Integer age;

}
