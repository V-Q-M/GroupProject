package main;

public class SettingsManager {

    // For the main menu
    public static String playText = "Play";
    public static String shopText = "Shop";
    public static String quitText = "Quit";
    public static String settingsText = "Settings";
    public static String helpText = "Help";

    // For the Settings
    public static String musicOnText = "Music on";
    public static String musicOffText = "Music off";
    public static String languageEnglishText = "Language English";
    public static String languageGermanText = "Language German";
    public static String debugOffText = "Hitboxes off";
    public static String debugOnText = "Hitboxes on (developer mode)";

    // For the Shop
    public static String wallUpgradeText = "Wall upgrade [PURCHASED]";
    public static String wallUpgradeDescriptionText = "Purchase two turrets defending their rows with cannonballs";
    public static String kingUpgradeText = "King upgrade [PURCHASED]";
    public static String kingUpgradeDescriptionText = "Purchase two more guards for the king - allowing him to attack 3 rows at once";
    public static String queenUpgradeText = "Queen upgrade [PURCHASED]";
    public static String queenUpgradeDescriptionText = "Purchase an ancient talisman for the queen - allowing her to heal on enemies";

    // General UI text
    public static String pressEscapeText = "Press ESCAPE to return to the main menu";
    public static String pressLeftText = "Press <- to view previous page";
    public static String pressRightText = "Press -> to view next page";

    // Help page
    public static String welcomeText = "Welcome to Chess Defense!";
    public static String controlsText = "Controls:";
    public static String piecesText = "Pieces Overview:";
    public static String kingNameText = "The King";
    public static String queenNameText = "The Queen";
    public static String rookNameText = "The Rook";
    public static String knightNameText = "The Knight";
    public static String bishopNameText = "The Bishop";
    public static String[] helpTextArray = {"The enemy Chess Pieces have started an assault on your castle and you must hold",
                                            "them off for as long as possible! Will you be strong enough?",
                                            "Use the WASD or Arrow-Keys to move your pieces.",
                                            "Press SPACE to perform an attack. Each piece has it's own unique attack.",
                                            "Press ESC to open the Pause menu.",
                                            "Remember. Every few seconds your piece gets automatically switched.",
                                            "Be prepared.",
                                            "Having mastered artillery - the rook has proven to be a reliable range option,",
                                            "capable of defeating his enemies from a safe distance.",
                                            "Having lost his sword - the knight relies on his mighty stead to strike fear",
                                            "into his enemies.",
                                            "",

    };


    public static boolean musicOff = false;
    public static boolean languageGerman = false;
    public static boolean debugMode = false;


    public static void writeSettings(){
        String[] newSettings = new String[3];
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

        FileManager.writeLinesToTempFile(newSettings);
    }

    public static void switchToGerman(){
        languageGerman = true;
        playText = "Start";
        shopText = "Shop";
        quitText = "Verlassen";
        settingsText = "Einstellungen";
        helpText = "Hilfe";

        musicOnText = "Musik an";
        musicOffText = "Musik aus";
        languageEnglishText = "Sprache Englisch";
        languageGermanText = "Sprache Deutsch";
        debugOffText = "Hitboxen aus";
        debugOnText = "Hitboxen an (Entwicklermodus)";

        wallUpgradeText = "Mauer upgrade [GEKAUFT]";
        wallUpgradeDescriptionText = "Kaufe zwei Tuerme, welche ihre Reihen mit Kannonkugeln verteidigen.";
        kingUpgradeText = "König upgrade [GEKAUFT]";
        kingUpgradeDescriptionText = "Kaufe zwei weitere Wachen fuer den König, wodurch er 3 Reihen angreifen kann.";
        queenUpgradeText = "Dame upgrade [GESPERRT]";
        queenUpgradeDescriptionText = "Kaufe einen antiken Talisman für die Dame, wodurch sie sich an Feinden heilen kann";

        // General UI text
        pressEscapeText = "Drücke ESC um zum Menü zu gehen";
        pressLeftText = "Drücke <- um zurück zu gehen";
        pressRightText = "Drücke -> um weiter zu gehen";

        // Help page
        welcomeText = "Willkommen bei Chess Defense!";
        controlsText = "Steuerung:";
        piecesText = "Übersicht der Figuren:";
        kingNameText = "Der König";
        queenNameText = "Die Dame";
        rookNameText = "Der Turm";
        knightNameText = "Der Springer";
        bishopNameText = "Der Läufer";

        helpTextArray[0] = "Die feindlichen Schachfiguren haben einen Angriff auf dein Schloss gestartet und";
        helpTextArray[1] = "du musst sie so lange wie möglich aufhalten! Wirst du stark genug sein?";
        helpTextArray[2] = "Verwende WASD oder die Pfeiltasten um deine Figuren zu bewegen.";
        helpTextArray[3] = "Drücke LEER um anzugreifen. Jede Figur hat ihre eigene Attacke.";
        helpTextArray[4] = "Drücke ESC um zu pausieren.";
        helpTextArray[5] = "Denke daran, dass alle paar Sekunden deine Figur gewechselt wird!";
        helpTextArray[6] = "Sei vorbereitet.";
        helpTextArray[7] = "Als ein Meister der Artillery hat der Turm sich als eine zuverlässige Fernkampfeinheit";
        helpTextArray[8] = "bewiesen, welche aus sicherer Distanz seine Feinde besiegen kann";
        helpTextArray[9] = "Seitdem der Springer sein Schwert verloren hat ist er auf sein mächtiges Ross angewiesen,";
        helpTextArray[10] = "welches seinen Feinden Furcht und Schrecken bereitet.";
    }
    public static void switchToEnglish(){
        languageGerman = false;
        playText = "Play";
        shopText = "Shop";
        quitText = "Quit";
        settingsText = "Settings";
        helpText = "Help";

        musicOnText = "Music on";
        musicOffText = "Music off";
        languageEnglishText = "Language English";
        languageGermanText = "Language German";
        debugOffText = "Hitboxes off";
        debugOnText = "Hitboxes on (developer mode)";

        wallUpgradeText = "Wall upgrade [PURCHASED]";
        wallUpgradeDescriptionText = "Purchase two turrets defending their rows with cannonballs";
        kingUpgradeText = "King upgrade [PURCHASED]";
        kingUpgradeDescriptionText = "Purchase two more guards for the king - allowing him to attack 3 rows at once";
        queenUpgradeText = "Queen upgrade [PURCHASED]";
        queenUpgradeDescriptionText = "Purchase an ancient talisman for the queen - allowing her to heal on enemies";

        // General UI text
        pressEscapeText = "Press ESCAPE to return to the main menu";
        pressLeftText = "Press <- to view previous page";
        pressRightText = "Press -> to view next page";

        // Help page
        welcomeText = "Welcome to Chess Defense!";
        controlsText = "Controls:";
        piecesText = "Pieces Overview:";
        kingNameText = "The King";
        queenNameText = "The Queen";
        rookNameText = "The Rook";
        knightNameText = "The Knight";
        bishopNameText = "The Bishop";

        helpTextArray[0] = "The enemy Chess Pieces have started an assault on your castle and you must hold";
        helpTextArray[1] = "them off for as long as possible! Will you be strong enough?";
        helpTextArray[2] = "Use the WASD or Arrow-Keys to move your pieces.";
        helpTextArray[3] = "Press SPACE to perform an attack. Each piece has it's own unique attack.";
        helpTextArray[4] = "Press ESC to open the Pause menu.";
        helpTextArray[5] = "Remember. Every few seconds your piece gets automatically switched!";
        helpTextArray[6] = "Be prepared.";
        helpTextArray[7] = "Having mastered artillery - the rook has proven to be a reliable range option,";
        helpTextArray[8] = "capable of defeating his enemies from a safe distance.";
        helpTextArray[9] = "Having lost his sword - the knight relies on his mighty stead to strike fear";
        helpTextArray[10] = "into his enemies.";
    }
}
