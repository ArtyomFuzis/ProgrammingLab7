package com.fuzis.proglab.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Main {
    @FXML
    private Button btn_home;
    @FXML
    private void home(){
        GuiApp.setPage("home");
    }
    @FXML
    private void account(){
        GuiApp.setPage("auth");

    }
    @FXML
    private void language(){
        GuiApp.setPage("language");

    }
}
