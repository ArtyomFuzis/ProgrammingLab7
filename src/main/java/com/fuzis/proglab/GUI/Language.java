package com.fuzis.proglab.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
    @FXML
    private ResourceBundle resources;
    @FXML
    private ToggleGroup lang;
    @FXML
    private RadioButton toggle_ru;
    @FXML
    private RadioButton toggle_en_in;
    @FXML
    private RadioButton toggle_bg;
    @FXML
    private RadioButton toggle_mk;
    @FXML
    private Button btn_submit_lang;
    @FXML
    private void submit()
    {
        Locale locale;
        var selected_toggle = lang.getSelectedToggle();

        if(selected_toggle == toggle_en_in) locale = new Locale("en","IN");
        else if(selected_toggle == toggle_bg) locale = new Locale("bg","BG");
        else if(selected_toggle == toggle_mk) locale = new Locale("mk","MK");
        else locale = new Locale("ru","RU");
        GuiApp.setRoot("main",locale);
        GuiApp.setPage("language");
    }
    @FXML
    private void initialize()
    {
        switch (GuiApp.locale.getLanguage() + "_" + GuiApp.locale.getCountry())
        {
            case "ru_RU":
                toggle_ru.setSelected(true);
                break;
            case "en_IN":
                toggle_en_in.setSelected(true);
                break;
            case "bg_BG":
                toggle_bg.setSelected(true);
                break;
            case "mk_MK":
                toggle_mk.setSelected(true);
                break;
        }
        btn_submit_lang.requestFocus();
    }
}
