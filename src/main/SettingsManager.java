package main;

public class SettingsManager {

    public static boolean musicOff = false;
    public static boolean languageGerman = false;
    public static boolean debugMode = false;


    public static void writeSettings(){
        String newSettings[] = new String[3];
        if (musicOff){
            newSettings[0] = "music off";
        } else {
            newSettings[0] = "music on";
        }
        if (languageGerman){
            newSettings[1] = "language german";
        } else {
            newSettings[1] = "language english";
        }
        if (debugMode){
            newSettings[2] = "debug on";
        } else {
            newSettings[2] = "debug off";
        }

        if (newSettings != null){
            FileManager.writeLinesToTempFile(newSettings);
        } else {
            System.out.println("NO SETTINGS APPLIED");
        }
    }
}
