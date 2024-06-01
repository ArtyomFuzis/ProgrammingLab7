package com.fuzis.proglab.Server;

import com.fuzis.proglab.Annotations.InteractiveCommand;
import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Server.Collection.CharacterCollectionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ServerConsole {
    public static final Logger logger = LoggerFactory.getLogger(ServerConsole.class);

    record ScriptData(Scanner file, String prev_key) {
    }

    public static HashMap<String, ScriptData> executing_scripts = new HashMap<>();
    public static String cur_script = null;
    public static CharacterCollectionSQL char_col;
    public static Boolean supress_inp_invite = false;
    public static Scanner scan;
    public static Scanner in_scan;
    public static Queue<String> history = new LinkedList<>();


    public static void println_supress(Object a) {
        if (supress_inp_invite) return;
        println(a);
    }

    public static void print_supress(Object a) {
        if (supress_inp_invite) return;
        print(a);
    }

    public static void print(Object a) {
        System.out.print(a);
    }

    public static void println(Object a) {
        System.out.println(a);
    }

    public static void println() {
        System.out.println();
    }

    public static class Cmds {
        public static class IDCharacter {
            public DefaultCartoonPersonCharacter character;
            public String id;

            public IDCharacter(String id, DefaultCartoonPersonCharacter character) {
                this.id = id;
                this.character = character;
            }
        }


        @InteractiveCommand(args = {0, 1}, usage = {"help - вывод справки по всем командам", "help <команда> - вывод справки по определенной команде"}, help = "Выводит справку по командам интерактивного режима")
        public void help(List<String> argc) {
            if (argc.isEmpty()) {
                println("Все команды, доступные в интерактивном режиме:");
                println();
                for (var el : Cmds.class.getMethods()) {
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    if (anot == null) continue;
                    println("-----------Команда-----------");
                    println(el.getName() + ": " + anot.help());
                    println("--------Использование--------");
                    //for (var el2 : anot.usage()) println(el2);
                    Arrays.stream(anot.usage()).forEach(ServerConsole::println);
                    println();
                }
            } else {
                try {
                    var el = Cmds.class.getMethod(argc.get(0), List.class);
                    var anot = el.getAnnotation(InteractiveCommand.class);
                    println("--------Использование--------");
                    //for (var el2 : anot.usage()) println(el2);
                    Arrays.stream(anot.usage()).forEach(ServerConsole::println);
                } catch (NoSuchMethodException ex) {
                    logger.error("No such command");
                }
            }
        }

        @InteractiveCommand(args = {0}, usage = {"show - вывод всех элементов коллекции"}, help = "Выводит элементы коллекции")
        public void show(List<String> argc) {
            if (char_col.getCharacters().isEmpty()) logger.info("It is empty");
            /*for (var el : char_col.getCharacters().keySet()) {
                println(el + ": " + char_col.getCharacters().get(el));
            }*/
            char_col.getCharacters().keySet().stream().forEach((x) -> println(x + ": " + char_col.getCharacters().get(x)));
        }

        public Cmds.IDCharacter add_interactive() {
            InteractiveInput inp;
            inp = new InteractiveInput(scan, ServerConsole::println_supress, ServerConsole::print_supress);
            var id = inp.id_interactive();
            if (char_col.getCharacters().containsKey(id)) {
                println_supress("Объект с данным id уже существует, попробуйте использовать update");
                return null;
            }
            String name = inp.name_interactive();
            Sex sex = inp.sex_interactive();
            String quote = inp.quote_interactive();
            Double height = inp.height_interactive();
            Double weight = inp.weight_interactive();
            Popularity popul = inp.popularity_interactive();
            String description = inp.description_interactive();
            Double age = inp.age_interactive();
            Integer health = inp.health_interactive();
            Boolean isAnimeCharacter = inp.isAnimeCharacter_interactive();
            List<String> additionalNames = inp.additionalnames_interactive();
            HashMap<String, Opinion> opinions = inp.opinions_interactive();
            return new Cmds.IDCharacter(id, new DefaultCartoonPersonCharacter(id, name, sex, quote, opinions, additionalNames, height, weight, isAnimeCharacter, popul, description, age, health));
        }

        @InteractiveCommand(args = {0}, usage = {"add - добавление персонажа в коллекцию, значение вводится построчно:", "<id> - строка-индификатор", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Производит добавления элемента в коллекцию")
        public void add(List<String> argc) {
            var new_charac = add_interactive();
            if (new_charac == null) return;
            char_col.add(new_charac.id, new_charac.character, ServerData.admin_id);
            logger.info("Successful add");
            StateMachine.get_instance().update(new Object[]{AppData.UpdateType.Add,new_charac.character});

        }

        @InteractiveCommand(args = {1}, usage = {"update <id>- изменение персонажа, содержащегося в коллекции, можно изменять конкретные поля, в остальных останутся предыдущие значения, end - для выхода", "<name> - имя", "<sex> пол персонажа, элемент из перечисления Sex", "<quote> - строка, цитата персонажа", "<height> - рост персонажа", "<weight> - вес персонажа", "<popularity> - популярность персонажа, элемент из перечисления Popularity", "<description> - строка, описание персонажа", "<age> - возраст персонажа", "<health> - значение здоровья персонажа в целочисленных условных единицах", "<isAnimeCharacter> - является ли アニメ персонажем, значение Yes/No", "<additionalNames> - строка, дополнительные имена персонажа, перечисление через запятую", "<opinions> - мнения о других персонажах (не обязательно из коллекции) в виде <имя>:<отношение>, <имя2>:<отношение2>... отношение - значение из перечисления Opinion"}, help = "Изменяет элемент в коллекции")
        public void update(List<String> argc) {
            InteractiveInput inp;
            inp = new InteractiveInput(scan, ServerConsole::println_supress, ServerConsole::print_supress);
            String id = argc.get(0);
            var charac_o = char_col.getCharacter(id);
            if (charac_o == null) {
                logger.error("Character not found");
                return;
            }
            var charac = new DefaultCartoonPersonCharacter(charac_o);
            while (true) {
                var str = inp.type_interactive();
                logger.info("Updating: " + str);
                switch (str) {
                    case "name" -> charac.setName(inp.name_interactive());
                    case "sex" -> charac.setSex(inp.sex_interactive());
                    case "quote" -> charac.setQuote(inp.quote_interactive());
                    case "opinions" -> charac.setOpinions(inp.opinions_interactive());
                    case "additionalnames" -> charac.setAdditionalNames(inp.additionalnames_interactive());
                    case "height" -> charac.setHeight(inp.height_interactive());
                    case "weight" -> charac.setWeight(inp.weight_interactive());
                    case "age" -> charac.setAge(inp.age_interactive());
                    case "health" -> charac.setHealth(inp.health_interactive());
                    case "isanimecharacter" -> charac.setAnimeCharacter(inp.isAnimeCharacter_interactive());
                    case "popularity" -> charac.setPopularity(inp.popularity_interactive());
                    case "description" -> charac.setDescription(inp.description_interactive());
                    case "end" -> {
                        char_col.deleteCharacter(id, ServerData.admin_id);
                        char_col.add(id, charac, ServerData.admin_id);
                        logger.info("End update");
                        StateMachine.get_instance().update(new Object[]{AppData.UpdateType.Update,charac});
                        return;
                    }
                    default -> logger.error("Field not found!!!");
                }
            }
        }

        @InteractiveCommand(args = {0}, usage = {"exit - завершить работу интерактивного режима"}, help = "Осуществляет выход из программы/подпрограммы")
        public void exit(List<String> argc) {
            logger.info("Shutting down...");
            System.exit(0);
        }

        @InteractiveCommand(args = {0}, usage = {"clear - полная очистка коллекции"}, help = "Осуществляет очистку коллекции")
        public void clear(List<String> argc) {
            char_col.clear(ServerData.admin_id);
            logger.info("Successful clearing");
            StateMachine.get_instance().update(new Object[]{AppData.UpdateType.Clear,ServerData.admin_id});
        }

        @InteractiveCommand(args = {1}, usage = {"remove_by_id <id> - удалить элемент с указанным id"}, help = "Удаляет указанный элемент")
        public void remove_by_id(List<String> argc) {
            if (null == char_col.deleteCharacter(argc.get(0), ServerData.admin_id)) logger.error("key not found");
            else {
                logger.info("Successful remove");
                StateMachine.get_instance().update(new Object[]{AppData.UpdateType.Remove,argc.get(0)});
            }
        }

        @InteractiveCommand(args = {0}, usage = {"add_if_max - добавляет новый элемент если он больше любого другого в коллекции, формат ввода объекта, такой же как и у add"}, help = "Добавляет элемент если он максимальный")
        public void add_if_max(List<String> argc) {
            var new_charac = add_interactive();
            if (char_col.getCharacters().values().stream().allMatch(x -> x.compareTo(new_charac.character) <= 0)) {
                char_col.add(new_charac.id, new_charac.character, ServerData.admin_id);
                logger.info("Successful add");
                StateMachine.get_instance().update(new Object[]{AppData.UpdateType.Add,new_charac.character});
            } else {
                logger.info("It is lower than something -> not added");
            }
        }

        @InteractiveCommand(args = {0}, usage = {"add_if_min - добавляет новый элемент если он меньше любого другого в коллекции, формат ввода объекта, такой же как и у add"}, help = "Добавляет элемент если он минимальный")
        public void add_if_min(List<String> argc) {
            var new_charac = add_interactive();
            if (char_col.getCharacters().values().stream().allMatch(x -> x.compareTo(new_charac.character) >= 0)) {
                char_col.add(new_charac.id, new_charac.character, ServerData.admin_id);
                logger.info("Successful add");
                StateMachine.get_instance().update(new Object[]{AppData.UpdateType.Add,new_charac.character});
            } else {
                logger.info("It is bigger than something -> not added");
            }
        }

        @InteractiveCommand(args = {0}, usage = {"min_by_age - выводит какой-то объект из коллекции, значение поля age которого - минимально"}, help = "Выводит персонажа, минимального по возрасту")
        public void min_by_age(List<String> argc) {
            if (char_col.size() == 0) {
                logger.info("Collection is empty");
                return;
            }
            var res = char_col.getCharacters().keySet().stream().map((x) -> new Cmds.IDCharacter(x, char_col.getCharacter(x))).min((o1, o2) -> {
                if (Objects.equals(o1.character.getAge(), o2.character.getAge())) return 0;
                if (o1.character.getAge() == null) return 1;
                if (o2.character.getAge() == null) return -1;
                if (o1.character.getAge() > o2.character.getAge()) return 1;
                return -1;
            }).get();
            println(res.id + ": " + res.character);
        }

        public static record SSPair(String one, Object two) {

        }

        @InteractiveCommand(args = {0}, usage = {"print_field_descending_name - выводит имена элементов из коллекции, отсортированные по убыванию"}, help = "Выводит имена персонажей, в порядке убывания")
        public void print_field_descending_name(List<String> argc) {
            if (char_col.getCharacters().isEmpty()) {
                logger.info("Collection is empty");
                return;
            }
            var cmp = new Comparator<SSPair>() {
                @Override
                public int compare(SSPair o1, SSPair o2) {
                    return -((String) o1.two).compareTo((String) o2.two);
                }
            };
            char_col.getCharacters().keySet().stream()
                    .map((x) -> new Cmds.SSPair(x, char_col.getCharacter(x).getName()))
                    .sorted(cmp)
                    .forEach((x) -> println(x.one + ": " + x.two));
        }

        @InteractiveCommand(args = {0}, usage = {"print_field_ascending_health - выводит здоровье элементов из коллекции, отсортированные по возрастанию"}, help = "Выводит здоровье персонажей, в порядке возрастания")
        public void print_field_ascending_health(List<String> argc) {
            if (char_col.getCharacters().isEmpty()) {
                logger.info("Collection is empty");
                return;
            }
            (char_col.getCharacters().keySet().stream().map((x) -> new Cmds.SSPair(x, char_col.getCharacter(x).getHealth()))).sorted(((o1, o2) -> {
                if (o1.two == o2.two && o1.two == null) return 0;
                if (o1.two == null) return 1;
                if (o2.two == null) return -1;
                return ((Integer) o1.two).compareTo((Integer) o2.two);
            })).forEach((x) -> println(x.one + ": " + x.two));
        }

        @InteractiveCommand(args = {0}, usage = {"info - выводит некоторую информацию о коллекции"}, help = "Выводит информацию о коллекции персонажей")
        public void info(List<String> argc) {
            println(char_col.getInfo());
        }

        @InteractiveCommand(args = {1}, usage = {"execute_script <filename> - запускает скрипт по адресу <filename>"}, help = "Запускает скрипт")
        public void execute_script(List<String> argc) {
            if (executing_scripts.containsKey(argc.get(0))) {
                logger.error("Script is already executing");
                return;
            }
            try {
                File f = new File(argc.get(0));
                scan = new Scanner(f);
                logger.info("Start executing script: " + argc.get(0));
                supress_inp_invite = true;
                executing_scripts.put(argc.get(0), new ScriptData(scan, cur_script));
                cur_script = argc.get(0);
            } catch (FileNotFoundException ex) {
                logger.error("File not found or unable to read");
            }
        }

        @InteractiveCommand(args = {2}, usage = {"register <пользователь> <пароль> - добавить пользователя с указанным именем и паролем"}, help = "Регистрация нового пользователя")
        public void register(List<String> argc) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-224");
                var to_encode = argc.get(1) + AppData.salt;
                byte[] encodedhash = digest.digest(to_encode.getBytes(StandardCharsets.UTF_8));
                String pass = bytesToHex(encodedhash);
                String user = argc.get(0);
                PreparedStatement st = CharacterCollectionSQL.con.prepareStatement("INSERT INTO auth (username,password) VALUES (?,?)");
                st.setString(1, user);
                st.setString(2, pass);
                try {
                    st.executeUpdate();
                    logger.info("Successful registration");
                } catch (SQLException e) {
                    try {
                        st.executeUpdate();
                        logger.info("Successful registration");
                    } catch (SQLException e2) {
                        logger.error("User with this id already exists");
                    }
                }
            } catch (NoSuchAlgorithmException ex) {
                logger.error("Такого алгоритма нет.... WHAT?!");
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }

        }

        @InteractiveCommand(args = {1}, usage = {"delete_user <пользователь> - удаляет пользователя с указанным именем"}, help = "Удаление пользователя")
        public void delete_user(List<String> argc) {
            try {
                String user = argc.get(0);
                PreparedStatement st = CharacterCollectionSQL.con.prepareStatement("DELETE FROM auth WHERE username=?");
                st.setString(1, user);
                try {
                    int res = st.executeUpdate();
                    if (res == 0) logger.warn("No such user");
                    else logger.info("Successful deletion");
                } catch (SQLException e) {
                    try {
                        var res = st.executeUpdate();
                        if (res == 0) logger.warn("No such user");
                        else logger.info("Successful deletion");
                    } catch (SQLException e2) {
                        logger.error("User with this id already exists");
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static void start() {
        char_col = CharacterCollectionSQL.getInstance();
        Cmds cmd_class = new Cmds();
        logger.info("Interactive mode started");
        in_scan = new Scanner(System.in);
        supress_inp_invite = false;
        while (scan == null || !scan.equals(in_scan)) {
            scan = in_scan;
            while (scan.hasNext()) {
                String pre = scan.nextLine().trim();
                if (pre.trim().isEmpty()) continue;
                String[] cmd = pre.split("\\s+");
                try {
                    var cmd_func = Cmds.class.getMethod(cmd[0], List.class);
                    var args_size = cmd_func.getAnnotation(InteractiveCommand.class).args();
                    if (Arrays.stream(args_size).noneMatch(x -> x == cmd.length - 1)) {
                        logger.error("Wrong count of arguments");
                        var arg = new ArrayList<String>();
                        arg.add(cmd[0]);
                        cmd_class.help(arg);
                        continue;
                    }
                    if (cmd.length == 1) cmd_func.invoke(cmd_class, new ArrayList<String>());
                    else {
                        cmd_func.invoke(cmd_class, Arrays.stream(Arrays.copyOfRange(cmd, 1, cmd.length)).toList());
                    }
                    history.add(pre);
                    if (history.size() > 14) {
                        history.poll();
                    }
                } catch (NoSuchMethodException ex) {
                    logger.error("No such command");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logger.error("Command execution error");
                }
                while (!scan.hasNext()) {
                    var prev = executing_scripts.get(cur_script).prev_key;
                    if (prev != null) {
                        scan = executing_scripts.get(prev).file;
                        executing_scripts.remove(cur_script);
                        cur_script = prev;
                    } else {
                        executing_scripts.remove(cur_script);
                        cur_script = null;
                        logger.info("End script execution");
                        break;
                    }
                }
            }
        }
    }

    public static void start_console() {
        var thr = new Thread(ServerConsole::start);
        thr.setName("ConsoleInteractive");
        thr.start();
    }
}
