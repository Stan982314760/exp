package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.ExpClass;
import cn.think.in.java.open.exp.classloader.ExpPluginMetaService;
import cn.think.in.java.open.exp.classloader.PluginMetaFat;
import cn.think.in.java.open.exp.classloader.support.UniqueNameUtil;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author cxs
 **/
@Slf4j
public class ExpAppContextImpl implements ExpAppContext {

    ExpPluginMetaService metaService;
    ObjectStore objectStore;


    public void setPluginMetaService(ExpPluginMetaService expPluginMetaService) {
        this.metaService = expPluginMetaService;
    }

    public void setObjectStore(ObjectStore objectStore) {
        this.objectStore = objectStore;
    }

    @Override
    public Plugin load(File file) throws Throwable {
        PluginMetaFat install = metaService.install(file);
        objectStore.registerCallback(install.getPluginBeanRegister(), install.getPluginId());
        log.info("安装加载插件 {}", install.getPluginId());
        return install.conv();
    }

    @Override
    public void unload(String pluginId) throws Exception {
        objectStore.unRegisterCallback(pluginId);
        metaService.unInstall(pluginId);
        log.info("卸载插件 {}", pluginId);
    }

    @Override
    public <P> List<P> get(String extCode) {
        try {
            List<ExpClass<P>> classes = metaService.get(extCode);
            List<P> result = new ArrayList<>();
            for (ExpClass<P> aClass : classes) {
                P bean = objectStore.getObject(UniqueNameUtil.getName(aClass.getAClass(), aClass.getPluginId()));
                if (bean != null) {
                    result.add(bean);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <P> List<P> get(Class<P> pClass) {
        return get(pClass.getName());
    }

    @Override
    public <P> P get(String extCode, String pluginId) {
        try {
            ExpClass<P> classZ = metaService.get(extCode, pluginId);
            return objectStore.getObject(UniqueNameUtil.getName(classZ.getAClass(), classZ.getPluginId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}