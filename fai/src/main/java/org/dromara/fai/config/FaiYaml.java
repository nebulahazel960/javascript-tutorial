package org.dromara.fai.config;

import cn.hutool.core.annotation.scanner.FieldAnnotationScanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FaiYaml {
    
    private FaiGlobalConfiguration fai;

    public FaiGlobalConfiguration getFai() {
        return fai;
    }

    public void setFai(FaiGlobalConfiguration fai) {
        this.fai = fai;
    }

    public static FaiGlobalConfiguration loadConfig(InputStream in) {
        final YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        yamlFactory.configure(YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS, true);
        final ObjectMapper mapper = new ObjectMapper(yamlFactory);

        try {
            final FaiYaml faiYaml = mapper.readValue(in, FaiYaml.class);
            final FaiGlobalConfiguration fai = faiYaml.getFai();
            final List<FaiChildConfiguration> models = fai.getModels();
            for (final FaiChildConfiguration model : models) {
                model.setParent(fai);
                fai.modelMap.put(model.getId(), model);
            }
            final List<FaiPromptConfiguration> prompts = fai.getPrompts();
            for (final FaiPromptConfiguration prompt : prompts) {
                fai.promptMap.put(prompt.getId(), prompt);
            }
            return fai;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
