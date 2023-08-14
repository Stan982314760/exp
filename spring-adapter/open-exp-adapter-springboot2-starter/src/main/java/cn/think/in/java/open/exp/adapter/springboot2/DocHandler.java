package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.document.api.ExpDocContext;
import cn.think.in.java.open.exp.document.api.model.ExtDocInterface;
import lombok.Builder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/14
 * @version 1.0
 **/
@Builder
public class DocHandler {

    Environment environment;

    public DocHandler(Environment environment) {
        this.environment = environment;
    }

    public void init() {
        String value = environment.getProperty("exp.document.doc.scan.path");
        if (StringUtil.isEmpty(value)) {
            return;
        }
        if (ExpDocContext.getSpi() == null) {
            return;
        }
        List<ExtDocInterface> extDocList = ExpDocContext.getSpi().getExtDocList(value);
        System.out.println("extDocList " + extDocList);
    }
}
