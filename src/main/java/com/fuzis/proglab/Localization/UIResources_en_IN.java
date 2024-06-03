package com.fuzis.proglab.Localization;

import java.util.ListResourceBundle;

public class UIResources_en_IN extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private final Object[][] contents =
            {
                    {"auth.head", "Authorization"},
                    {"auth.usernamePlaceholder", "Enter the user name..."},
                    {"auth.passwordPlaceholder", "Enter the password..."},
                    {"auth.submit", "Log in"},
                    {"main.head","CringeLab v0.0.1"},
                    {"lang.head","Language options"},
                    {"lang.submit","Confirm"},
                    {"error","Erroe"},
                        {"auth.error.body","The username / password do not match,\n" +
                                "double-check the correctness\n" +
                                "of filling in the fields and the keyboard layout."},
                    {"auth.success.body","The login was successful,\n" +
                            "you have logged in!"},
                    {"auth.as","Logged in as:"},
                    {"success","Success"},
                    {"home.update.btn","Update"},
                    {"home.add.btn","Add"},
                    {"home.remove.btn","Delete"},
                    {"id.empty","ID can't be empty"},
                    {"error.unknown","Unknown error"},
                    {"auth.no","Not authorized"},
                    {"auth.problem","There is no access to perform this action. Check that you are logged in and the object belongs to you"},
                    {"home.tab.scene","Graphical representation"},
                    {"home.tab.table","Table"},
                    {"home.clear.btn","Clear"},
                    {"home.filter.btn","Filter"},
                    {"home.clfil.btn","Reset filter"},
                    {"clear.conform","Are you sure you want to delete all the items in your collection?"},
                    {"CONFORMATION","Confirmation"},
                    {"YES","Yes"},
                    {"NO","No"},
                    {"home.clfld.btn","Reset"},
                    {"home.filter2.btn","Editing a row"},
                    {"home.img.btn","Image"},
                    {"fc.home.img","Choose an image"},
                    {"imgs","Images"},
                    {"edit.img.del","Delete image"},

            };
}
