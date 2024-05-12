package io.skydeck.gserver.engine;

import io.skydeck.gserver.annotation.AbilityName;
import io.skydeck.gserver.domain.AbilityBase;
import io.skydeck.gserver.domain.Player;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AbilityFactory implements ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    private Map<String, Class> skillClassMap = new HashMap<>();

    @PostConstruct
    public void loadSkillClassMap() {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = null;
        try {
            resources = resolver.getResources("classpath:/io/skydeck/gserver/domain/skill/*.class");
            for (Resource r : resources) {
                MetadataReader reader = metaReader.getMetadataReader(r);
                String className = reader.getClassMetadata().getClassName();
                Class clazz = Class.forName(className);
                AbilityName name = (AbilityName) clazz.getAnnotation(AbilityName.class);
                if (name == null) {
                    continue;
                }
                skillClassMap.put(name.value(), clazz);
            }
        } catch (Exception e) {
            log.error("can't load skill metadata", e);
            System.exit(-1);
        }
    }

    public AbilityBase newSkill(Player owner, String skillName) {
        Class clazz = skillClassMap.get(skillName);
        if (clazz == null) {
            return null;
        }
        AbilityBase ability = null;
        try {
            Constructor<AbilityBase> con = clazz.getDeclaredConstructor(Player.class);
            ability = con.newInstance(owner);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("can't instantiate skill {}",skillName, e);
            System.exit(-1);
        }
        return ability;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
