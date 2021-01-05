package me.zhaotb.web.service;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.config.SequenceConfig;
import me.zhaotb.web.config.SequenceRegisterConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author zhaotangbo
 * @date 2021/1/4
 */
@Slf4j
@Service
public class SequenceService {

    /**
     * 序列前缀
     */
    private static final String SEQ_PREFIX = "SEQ::";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SequenceRegisterConfig sequenceRegisterConfig;

    @PostConstruct
    public void init(){
        log.info("开始回写序列值");
        Map<String, SequenceConfig> configs = sequenceRegisterConfig.getConfigs();
        configs.forEach((k, v) -> {
            String column = v.getColumn();
            if (StringUtils.isBlank(column)) {
                column = "id";
            }
            Long lastSeq = jdbcTemplate.queryForObject("select max(" + column + ") from " + k, Long.class);
            log.info("{} 序列值为： {}", k, lastSeq);
            if (lastSeq == null) {
                lastSeq = v.getStart();
            }
            redisTemplate.opsForValue().set(SEQ_PREFIX + k, lastSeq.toString());
        });
        log.info("序列值回写完成");
    }

    public long next(String name) {
        return redisTemplate.opsForValue().increment(SEQ_PREFIX + name);
    }


}
