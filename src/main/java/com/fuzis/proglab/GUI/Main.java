package com.fuzis.proglab.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Main {
    @FXML
    private Button btn_home;
    @FXML
    public Label lbl_auth_as;
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
    @FXML
    private void initialize()
    {
        MainProperties.lbl_auth_as = lbl_auth_as;
    }
}
