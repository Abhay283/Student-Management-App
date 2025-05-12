import java.io.*;
import java.util.*;

class Student implements Serializable {
    private String name;
    private String rollNumber;
    private String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getRollNumber() { return rollNumber; }
    public String getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

class StudentManagementSystem {
    private List<Student> students;
    private final String fileName = "students.dat";

    public StudentManagementSystem() {
        students = loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(String rollNumber) {
        students.removeIf(s -> s.getRollNumber().equalsIgnoreCase(rollNumber));
        saveStudents();
    }

    public Student searchStudent(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(rollNumber)) return s;
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void editStudent(String rollNumber, String newName, String newGrade) {
        Student s = searchStudent(rollNumber);
        if (s != null) {
            s.setName(newName);
            s.setGrade(newGrade);
            saveStudents();
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving students.");
        }
    }

    private List<Student> loadStudents() {
        File file = new File(fileName);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading students.");
            return new ArrayList<>();
        }
    }
}

public class StudentManagementApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentManagementSystem sms = new StudentManagementSystem();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Remove Student");
            System.out.println("4. Search Student");
            System.out.println("5. Display All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> editStudent();
                case 3 -> removeStudent();
                case 4 -> searchStudent();
                case 5 -> displayAllStudents();
                case 6 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 6);
    }

    private static void addStudent() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter roll number: ");
        String roll = scanner.nextLine().trim();
        System.out.print("Enter grade: ");
        String grade = scanner.nextLine().trim();

        if (name.isEmpty() || roll.isEmpty() || grade.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        if (sms.searchStudent(roll) != null) {
            System.out.println("Student with this roll number already exists.");
            return;
        }

        Student student = new Student(name, roll, grade);
        sms.addStudent(student);
        System.out.println("Student added successfully.");
    }

    private static void editStudent() {
        System.out.print("Enter roll number of student to edit: ");
        String roll = scanner.nextLine().trim();
        Student student = sms.searchStudent(roll);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter new name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter new grade: ");
        String grade = scanner.nextLine().trim();

        if (name.isEmpty() || grade.isEmpty()) {
            System.out.println("Name and grade cannot be empty.");
            return;
        }

        sms.editStudent(roll, name, grade);
        System.out.println("Student details updated.");
    }

    private static void removeStudent() {
        System.out.print("Enter roll number to remove: ");
        String roll = scanner.nextLine().trim();
        Student student = sms.searchStudent(roll);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        sms.removeStudent(roll);
        System.out.println("Student removed successfully.");
    }

    private static void searchStudent() {
        System.out.print("Enter roll number to search: ");
        String roll = scanner.nextLine().trim();
        Student student = sms.searchStudent(roll);

        if (student != null) {
            System.out.println("Student Found: " + student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void displayAllStudents() {
        List<Student> students = sms.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("\n--- Student List ---");
            students.forEach(System.out::println);
        }
    }
}

