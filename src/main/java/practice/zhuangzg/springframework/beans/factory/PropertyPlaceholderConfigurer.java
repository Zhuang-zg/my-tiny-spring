package practice.zhuangzg.springframework.beans.factory;

import practice.zhuangzg.springframework.beans.BeansException;
import practice.zhuangzg.springframework.beans.PropertyValue;
import practice.zhuangzg.springframework.beans.PropertyValues;
import practice.zhuangzg.springframework.beans.factory.config.BeanDefinition;
import practice.zhuangzg.springframework.beans.factory.config.BeanFactoryPostProcessor;
import practice.zhuangzg.springframework.core.io.DefaultResourceLoader;
import practice.zhuangzg.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * @author: ZhuangZG
 * @date 2022/7/12 5:46
 * @Description:
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    private final static String DEFAULT_PLACEHOLDER_PREFIX = "${";

    private final static String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);
                PropertyValues propertyValues = beanDefinition.getPropertyValues();

                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) {
                        continue;
                    }

                    String strVal = (String) value;
                    StringBuilder builder = new StringBuilder(strVal);
                    int indexOfPrefix = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
                    int indexOfSuffix = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);

                    if (indexOfPrefix !=-1 && indexOfSuffix != -1 && indexOfPrefix < indexOfSuffix) {
                        String propertyKey = strVal.substring(indexOfPrefix + 2, indexOfSuffix);
                        String propertyVal = properties.getProperty(propertyKey);
                        builder.replace(indexOfPrefix, indexOfSuffix+1, propertyVal);
                        propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), builder.toString()));
                    }
                }
            }

        } catch (IOException e) {
            throw new BeansException("could not load properties", e);
        }

    }

    public void setLocation(String location) {
        this.location = location;
    }
}
