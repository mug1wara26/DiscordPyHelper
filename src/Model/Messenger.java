package Model;

import Controllers.ApplicationController;
import javafx.scene.control.TreeItem;


public class Messenger {
    private static ApplicationController applicationController;
    private static TreeItem<String> root;

    public static ApplicationController getApplicationController() {
        return applicationController;
    }

    public static void setApplicationController(ApplicationController applicationController) {
        Messenger.applicationController = applicationController;
    }

    public static TreeItem<String> getRoot() {
        return root;
    }

    public static void setRoot(TreeItem<String> root) {
        Messenger.root = root;
    }
}
