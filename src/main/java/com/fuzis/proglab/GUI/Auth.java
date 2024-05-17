package com.fuzis.proglab.GUI;

import com.fuzis.proglab.Client.ClientExecutionModule;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Auth
{
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label lbl_feedback_head_auth;
    @FXML
    private Label lbl_feedback_body_auth;
    @FXML
    private AnchorPane anchor_msg_auth;
    @FXML
    private PasswordField fld_password;
    @FXML
    private TextField fld_username;
    @FXML
    private void submit()
    {
        if(!ClientExecutionModule.request_auth(fld_username.getText(), fld_password.getText())) {
            anchor_msg_auth.setVisible(true);
            lbl_feedback_head_auth.setText(resources.getString("error"));
            lbl_feedback_body_auth.setText(resources.getString("error_auth_body"));
        }
        else
        {
            anchor_msg_auth.setVisible(false);
        }
    }
}
