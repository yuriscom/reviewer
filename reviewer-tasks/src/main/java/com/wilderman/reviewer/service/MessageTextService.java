package com.wilderman.reviewer.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageTextService {

    @Autowired
    ResourceLoader resourceLoader;

    static Map<String, String> messageTemplates = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath:text_messages/*.txt");
        for (Resource resource : resources) {
            String key = FilenameUtils.removeExtension(FilenameUtils.getName(resource.getFilename()));
            messageTemplates.put(key, getResourceContents(resource));
        }
    }

    public String parse(String name, Map<String, String> values) {
        String content = template(name);

        String parsedContent = StringReplacer.replace(content,
                Pattern.compile("%([^%]+)%"),
                m -> (Optional.ofNullable(values.get(m.group(1))).orElse(""))
        );

        return parsedContent;
    }

    public String template(String name) {
        return Optional.ofNullable(messageTemplates.get(name)).orElse("");
    }

    public Map<String, String> templates() {
        return messageTemplates;
    }

    private String getResourceContents(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
    }

    public static Map<String, String> getTemplates() {
        return messageTemplates;
    }
}
