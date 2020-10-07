package Model;

import Controllers.ApplicationController;

public class Messenger {
    private static ApplicationController applicationController;

    public static ApplicationController getApplicationController() {
        return applicationController;
    }

    public static void setApplicationController(ApplicationController applicationController) {
        Messenger.applicationController = applicationController;
    }
}
