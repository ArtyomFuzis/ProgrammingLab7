package com.fuzis.proglab.Localization;

import java.util.ListResourceBundle;

public class UIResources_mk_MK extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private final Object[][] contents =
            {
                    {"auth.head", "Овластување"},
                    {"auth.usernamePlaceholder", "Внесете го вашето корисничко име..."},
                    {"auth.passwordPlaceholder", "Внесете ја лозинката..."},
                    {"auth.submit", "Да влезам"},
                    {"main.head","CringeLab v0.0.1"},
                    {"lang.head","Јазични опции"},
                    {"lang.submit","Потврди"},
                    {"error","Грешка"},
                    {"auth.error.body","Најава/лозинка не се совпаѓаат,\n" +
                            "кога проверете ја исправноста\n" +
                            "пополнување полиња и распоред на тастатурата."},
                    {"auth.success.body","Вход произведен успешно, \nвы авторизовались!"},
                    {"auth.as","Овластен како:"},
                    {"success","Успех"},
                    {"home.update.btn","Промена"},
                    {"home.add.btn","Додадете"},
                    {"home.remove.btn","Избриши"},
                    {"id.empty","ID не може да биде празна"},
                    {"error.unknown","Неизвестная ошибка"},
                    {"auth.no","Не авторизован"},
                    {"auth.problem","Немате пристап да го извршите ова дејство. Проверете дали сте овластени и дали предметот ви припаѓа вам"},
                    {"home.tab.scene","Графички приказ"},
                    {"home.tab.table","Табела"},
                    {"home.clear.btn","Јасно"},
                    {"home.filter.btn","Филтер"},
                    {"home.clfil.btn","Ресетирајте го филтерот"},
                    {"clear.conform","Дали сте сигурни дека сакате да ги избришете сите ставки од вашата колекција??"},
                    {"CONFORMATION","Потврда"},
                    {"YES","Да"},
                    {"NO","Бр"},
                    {"home.clfld.btn","Ресетирај"},
                    {"home.filter2.btn","Уредување линија"},
                    {"home.img.btn","Слика"},
                    {"fc.home.img","Изберете слика"},
                    {"imgs","Слики"},
                    {"edit.img.del","Избришете ја сликата"},

            };
}
