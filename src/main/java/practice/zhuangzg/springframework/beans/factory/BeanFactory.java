package practice.zhuangzg.springframework.beans.factory;

import practice.zhuangzg.springframework.beans.BeansException;

/**
 * @author: ZhuangZG
 * @date 2022/6/16 22:46
 * @Description:
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;
}