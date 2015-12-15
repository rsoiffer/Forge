package graphics.loading;

import graphics.data.Model;
import java.util.HashMap;

public abstract class ModelContainer {

    private static HashMap<String, Model> modelMap = new HashMap();
    private static String path = "models/";
    private static String type = ".obj";

    public static Model loadModel(String name) {
        if (modelMap.containsKey(name)) {
            return modelMap.get(name);
        }
        modelMap.put(name, new Model(path + name + type));
        return modelMap.get(name);
    }
}
