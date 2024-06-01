package com.fuzis.proglab.GUI;


import com.fuzis.proglab.Client.ClientMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class GuiApp extends Application {
    private static Scene scene;
    public static VBox root;
    public static Locale locale;
    @Override
    public void start(Stage stage) throws IOException {
        setRoot("main",new Locale("ru","RU"));
        setPage("home");
        scene = new Scene(root.getParent(), 1600, 800);
        stage.setScene(scene);
        stage.show();
    }
    static void setPage(String fxml) {
        try {
            if(root.getChildren().size()>1)root.getChildren().remove(root.getChildren().size()-1);
            var root_page = (AnchorPane) loadFXML(fxml);
            root.getChildren().add(root_page);
        }
        catch (IOException ex)
        {
            System.out.println("Unable to load FXML: " + fxml);
            System.out.println("Error: " + ex.getMessage());
            System.exit(1);
        }
    }
    public static void outer_start(String[] args)
    {
        launch(args);
    }
    public static void setRoot(String fxml,Locale _locale)
    {
        try {
            locale = _locale;
            var _root = loadFXML(fxml);
            root = (VBox) ((AnchorPane) _root).getChildren().get(0);
            if(scene != null)scene.setRoot(root.getParent());
        }
        catch (IOException ex)
        {
            System.out.println("Unable to load FXML: " + fxml);
            System.out.println("Error: " + ex.getMessage());
            System.exit(1);
        }
    }
    public static Parent loadFXML(String fxml) throws IOException {
        ResourceBundle bundle = ListResourceBundle.getBundle("com.fuzis.proglab.Localization.UIResources", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(GuiApp.class.getResource(fxml + ".fxml"),bundle);
        return fxmlLoader.load();
    }
}

