package ir.albino.client.features.modules;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import ir.albino.client.AlbinoClient;
import lombok.SneakyThrows;
import lombok.val;

public class ModuleManager {

    @SneakyThrows
    public void initModules() {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .scan()) {
            for (ClassInfo clz : scanResult.getClassesWithAnnotation(ModuleInfo.class)) {

                val loadClass = clz.loadClass();
                val annotation = loadClass.getAnnotation(ModuleInfo.class);
                val module = (Module) loadClass.getConstructor().newInstance();

                module.setName(annotation.module());
                module.setDescription(annotation.description());
                module.setVersion(annotation.version());
                module.setDraggable(annotation.draggable());

                module.onInit();

                AlbinoClient.instance.getLogger().info(String.format("Module Loaded %s - %s", annotation.module(), annotation.version()));
                AlbinoClient.instance.modules.add(module);

            }
        }
    }

    public Module getModuleByName(String name) {
        val module = AlbinoClient.instance.modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();
        return module.orElse(null);
    }
}