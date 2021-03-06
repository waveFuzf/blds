package com.example.blds.config;

import com.example.blds.util.ResourceLoader;
import freemarker.template.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class FreemarkerConfiguration {
    private static Configuration config = null;

    /**
     * 获取 FreemarkerConfiguration
     *
     * @Title: getConfiguation
     * @Description:
     * @return
     */
    public static synchronized Configuration getConfiguation() {
        if (config == null) {
            setConfiguation();
        }
        return config;
    }

    /**
     * 设置 配置
     * @Title: setConfiguation
     * @Description:
     */
    private static void setConfiguation() {
        config = new Configuration();
//        String path = ResourceLoader.getPath("");
        try {
            config.setDirectoryForTemplateLoading(new File(ResourceUtils.getFile("classpath:").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
