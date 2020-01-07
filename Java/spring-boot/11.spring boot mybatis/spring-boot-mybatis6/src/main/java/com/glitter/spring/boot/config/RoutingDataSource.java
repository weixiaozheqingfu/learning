package com.glitter.spring.boot.config;

import com.glitter.spring.boot.context.DatasourceContext;
import com.glitter.spring.boot.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${mysql.datasource.read-num}")
    private int readNum;

    @Override
    protected Object determineCurrentLookupKey() {
        String dsType = DatasourceContext.get();

        if (StringUtils.isBlank(dsType)) {
            log.info("默认使用了写库");
            return DatasourceContext.WRITE;
        }

        if (DatasourceContext.WRITE.equals(dsType)) {
            log.info("使用了写库");
            return DatasourceContext.WRITE;
        }

        //使用随机数决定使用哪个读库
        int num = NumberUtil.getRandom(0, readNum);
        log.info("使用了读库{}", num);

        return DatasourceContext.READ + num;
    }

}
