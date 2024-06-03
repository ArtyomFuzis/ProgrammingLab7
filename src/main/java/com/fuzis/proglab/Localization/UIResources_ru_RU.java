package com.fuzis.proglab.Localization;

import java.util.ListResourceBundle;

public class UIResources_ru_RU extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private final Object[][] contents =
            {
                    {"auth.head", "Авторизация"},
                    {"auth.usernamePlaceholder", "Введите имя пользователя..."},
                    {"auth.passwordPlaceholder", "Введите пароль..."},
                    {"auth.submit", "Войти"},
                    {"main.head","CringeLab v0.0.1"},
                    {"lang.head","Языковые параметры"},
                    {"lang.submit","Подтвердить"},
                    {"error","Ошибка"},
                    {"auth.error.body","Логин/пароль не совпадают, \nперепроверьте правильность \nзаполения полей и раскладку клавиатуры."},
                    {"auth.success.body","Вход произведен успешно, \nвы авторизировались!"},
                    {"auth.as","Авторизован как:"},
                    {"success","Успех"},
                    {"home.update.btn","Изменить"},
                    {"home.add.btn","Добавить"},
                    {"home.remove.btn","Удалить"},
                    {"id.empty","ID не может быть пустым"},
                    {"error.unknown","Неизвестная ошибка"},
                    {"auth.no","Не авторизован"},
                    {"auth.problem","Нет доступа для выполнения данного действия. Проверьте что вы авторизованы и объект принадлежит вам"},
                    {"home.tab.scene","Графическое представление"},
                    {"home.tab.table","Таблица"},
                    {"home.clear.btn","Очистить"},
                    {"home.filter.btn","Фильтр"},
                    {"home.clfil.btn","Сбросить фильтр"},
                    {"clear.conform","Вы уверены что желаете удалить все элементы своей коллекции?"},
                    {"CONFORMATION","Подтверждение"},
                    {"YES","Да"},
                    {"NO","Нет"},
                    {"home.clfld.btn","Сброс"},
                    {"home.filter2.btn","Редактирование строки"},
                    {"home.img.btn","Изображение"},
                    {"fc.home.img","Выберите изображение"},
                    {"imgs","Изображения"},
                    {"edit.img.del","Удалить изображение"},

            };
}
