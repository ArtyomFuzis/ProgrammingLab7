package com.fuzis.proglab.Localization;

import java.util.ListResourceBundle;

public class UIResources_bg_BG extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private final Object[][] contents =
            {
                    {"auth.head", "Авторизация"},
                    {"auth.usernamePlaceholder", "Въведете име на потребителя..."},
                    {"auth.passwordPlaceholder", "Въведете парола..."},
                    {"auth.submit", "Вход"},
                    {"main.head","CringeLab v0.0.1"},
                    {"lang.head","Языковые параметры"},
                    {"lang.submit","Потвърдете"},
                    {"error","Грешка"},
                    {"auth.error.body","Логин/парола не съвпадат,\n" +
                            "при проверка на коректността\n" +
                            "попълване на полета и клавиатурна подредба."},
                    {"auth.success.body","Успешен вход\n" +
                            "влезли сте!"},
                    {"auth.as","Упълномощен като:"},
                    {"success","Успех"},
                    {"home.update.btn","промяна"},
                    {"home.add.btn","Добавете"},
                    {"home.remove.btn","Изтрий"},
                    {"id.empty","ID не може да бъде празно"},
                    {"error.unknown","Неизвестна грешка"},
                    {"auth.no","Неоторизиран"},
                    {"auth.problem","Нямате достъп за извършване на това действие. Проверете дали сте упълномощени и обектът ви принадлежи"},
                    {"home.tab.scene","Графично представяне"},
                    {"home.tab.table","Таблица"},
                    {"home.clear.btn","Ясно"},
                    {"home.filter.btn","Филтър"},
                    {"home.clfil.btn","Нулирайте филтъра"},
                    {"clear.conform","Сигурни ли сте, че искате да изтриете всички елементи от вашата колекция??"},
                    {"CONFORMATION","Потвърждение"},
                    {"YES","Да"},
                    {"NO","Не"},
                    {"home.clfld.btn","Нулиране"},
                    {"home.filter2.btn","Редактиране на линия"},
                    {"home.img.btn","Изображение"},
                    {"fc.home.img","Изберете изображение"},
                    {"imgs","Изображения"},
                    {"edit.img.del","Изтриване на изображение"},

            };
}
