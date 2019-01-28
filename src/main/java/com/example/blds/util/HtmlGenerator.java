package com.example.blds.util;

import com.example.blds.config.FreemarkerConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class HtmlGenerator {

    public static String generate(String template, Map<String,Object> variables) throws IOException, TemplateException, IOException {

        Configuration config = FreemarkerConfiguration.getConfiguation();
        config.setDefaultEncoding("UTF-8");
        //Template tp = configuration.getTemplate("overseaAssistance.html");
        Template tp = config.getTemplate(template);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        tp.setEncoding("UTF-8");
        tp.process(variables, writer);
        String htmlStr = stringWriter.toString();
        writer.flush();
        writer.close();
        return htmlStr;
    }
}
