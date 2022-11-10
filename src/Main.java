import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Main {


    public static void main(String[] args) throws EmptyStringException {

        TaskService taskService = new TaskService();
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.println("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            addTask(taskService, scanner);
                            break;
                        case 2:
                            removeTask(taskService, scanner);
                            break;
                        case 3:
                            getTaskByDay(taskService, scanner);
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.print("Выберите пункт меню из списка: ");
                }
            }
        }
    }

    private static LocalDate getDateFromUser(Scanner scanner) {
        LocalDate result = null;
        boolean forceUserToAnswer = true;
        while (forceUserToAnswer) {
            try {
                System.out.println("Введите дату задачи в формате dd.mm.yyyy: ");
                String date = scanner.nextLine();
                result = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                forceUserToAnswer = false;
            } catch (Exception e) {
                System.out.println("Вводи правильно! Еще раз!");
            }
        }
        return result;
    }

    private static LocalTime getTimeFromUser(Scanner scanner) {
        LocalTime result = null;
        boolean forceUserToAnswerTime = true;
        while (forceUserToAnswerTime) {
            try {
                System.out.println("Введите время задачи в формате hh:mm ");
                String time = scanner.nextLine();
                result = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
                forceUserToAnswerTime = false;
            } catch (Exception e) {
                System.out.println("Вводи правильно! Еще раз!");
            }
        }
        return result;
    }

    private static void addTask(TaskService taskService, Scanner scanner) throws EmptyStringException {
        System.out.println("Введите заголовок задачи: ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.println("Введите описание задачи: ");
        String description = scanner.nextLine();
        LocalDate taskDate = getDateFromUser(scanner);
        System.out.println("Введите время задачи в формате hh:mm ");
        String time = scanner.nextLine();
        LocalTime taskTime = getTimeFromUser(scanner);
        LocalDateTime resultDate = LocalDateTime.of(taskDate, taskTime);
        System.out.println("Введите тип задачи: Личный(1), Рабочий(2) ");
        int type = scanner.nextInt();
        Type taskType = type == 1 ? Type.PERSONAL : Type.WORK;
        System.out.println("Введите повторяемость задачи: ");
        System.out.println(" 0 - Не повторяется ");
        System.out.println(" 1 - Дневная ");
        System.out.println(" 2 - Недельная ");
        System.out.println(" 3 - Месячная ");
        System.out.println(" 4 - Годовая ");
        int typeTask = scanner.nextInt();
        switch (typeTask) {
            case 0:
                taskService.add(new Task(name, description, taskType, resultDate));
                break;
            case 1:
                taskService.add(new DailyTask(name, description, taskType, resultDate));
                break;
            case 2:
                taskService.add(new WeeklyTask(name, description, taskType, resultDate));
                break;
            case 3:
                taskService.add(new MonthlyTask(name, description, taskType, resultDate));
                break;
            case 4:
                taskService.add(new YearlyTask(name, description, taskType, resultDate));
                break;
            default:
                throw new RuntimeException("Нет такого типа задачи!");
        }
    }

    private static void removeTask(TaskService taskService, Scanner scanner) throws EmptyStringException {
        System.out.println("Введите id задачи, которую необходимо удалить:  ");
        int id = scanner.nextInt();
        taskService.remove(id);
    }

    private static void getTaskByDay(TaskService taskService, Scanner scanner) throws EmptyStringException {
        System.out.println("Введите дату задачи в формате dd.mm.yyyy: ");
        scanner.nextLine();
        String date = scanner.nextLine();
        LocalDate taskDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        var allTaskByDay = taskService.getAllByDate(taskDate);
        System.out.println("Список задач этого дня: ");
        for (Task task : allTaskByDay) {
            System.out.println(task);
        }
    }

    private static void printMenu() {
        System.out.println(
                "1. Добавить задачу\n" +
                        "2. Удалить задачу\n" +
                        "3. Получите задачи на указанный день\n" +
                        "0. Выход\n"
        );
    }
}