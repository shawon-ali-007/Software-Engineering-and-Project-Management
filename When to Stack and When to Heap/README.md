# When to Stack and When to Heap, Confusing!!

**Course/Task:** Java Lab Practice — Stack and Heap Memory  
**Language:** Java  
**Submission:** Submit the GitHub repository URL  
**Due:** Monday, 23:59  
**Total points:** 4

This repository contains both parts of the assignment:
1. Basic Level — `Student.java`
2. Intermediate Level — `Employee.java`

---

## Part 1: Basic Level — Demonstrate Stack and Heap Memory

### Problem
Create a `Student` class with an instance variable `age` and a method `display()`. Inside `display()`, declare a local variable `marks`. In `main()`, create a `Student` object and display its identity hash code, instance variable, and local variable.

### Source Code: `Student.java`

```java
class Student {
    int age = 20;

    void display() {
        int marks = 85;

        System.out.println("Instance Variable (age): " + age);
        System.out.println("Local Variable (marks): " + marks);
    }

    public static void main(String[] args) {
        Student s = new Student();

        System.out.println("Student Object Created");
        System.out.println("Object Identity Hash Code: "
                + System.identityHashCode(s));

        s.display();
    }
}
```

### Sample Input / Values
The values are set in the program:
- Age: `20`
- Marks: `85`

### Expected Output

The identity hash code is illustrative and may differ each time the program runs.

```text
Student Object Created
Object Identity Hash Code: 12345678
Instance Variable (age): 20
Local Variable (marks): 85
```

### Compile and Run

```bash
javac Student.java
java Student
```

### Concepts Demonstrated
- `age` is an instance field belonging to the `Student` object.
- `marks` is a local variable declared inside `display()`.
- `new Student()` creates a Student object, normally allocated on the heap.
- `System.identityHashCode(s)` returns an identity hash code; it is not the actual memory address.

---

## Part 2: Intermediate Level — Stack Frames, Heap Objects, and Inner Classes

### Problem
Create an `Employee` class with an instance variable `salary` and an instance method `display()`. Inside `display()`, declare a local variable `bonus` and define a local inner class named `Report` with a `show()` method.

The program must:
1. Create an `Employee` object and display its identity hash code.
2. Display `salary` and `bonus`.
3. Create a `Report` object and display its identity hash code.
4. Call `show()` to display the employee's salary.
5. Display the required memory concept summary.

### Source Code: `Employee.java`

```java
import java.util.Scanner;

class Employee {
    int salary;

    Employee(int salary) {
        this.salary = salary;
    }

    void display(int bonusInput) {
        int bonus = bonusInput;

        System.out.println("\nInside display() Method:");
        System.out.println("Salary (Instance Variable): " + salary);
        System.out.println("Bonus (Local Variable): " + bonus);

        class Report {
            void show() {
                System.out.println("Report: Employee salary is " + salary);
            }
        }

        Report r = new Report();

        System.out.println("\nReport Object Created");
        System.out.println("Report Identity Hash Code: "
                + System.identityHashCode(r));

        r.show();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter salary: ");
        int salary = input.nextInt();

        System.out.print("Enter bonus: ");
        int bonus = input.nextInt();

        Employee e = new Employee(salary);

        System.out.println("\nEmployee Object Created");
        System.out.println("Employee Identity Hash Code: "
                + System.identityHashCode(e));

        e.display(bonus);

        System.out.println("\nMemory Concept:");
        System.out.println("Employee object -> Heap");
        System.out.println("Report object -> Heap");
        System.out.println("salary -> Instance field");
        System.out.println("bonus -> Local variable in display()");
        System.out.println("show() -> Executes in its own method frame");

        input.close();
    }
}
```

### Sample Input

Enter the following values when prompted:

```text
30000
5000
```

### Expected Output

The identity hash codes below are examples; your actual values may differ.

```text
Enter salary: 30000
Enter bonus: 5000

Employee Object Created
Employee Identity Hash Code: 12345678

Inside display() Method:
Salary (Instance Variable): 30000
Bonus (Local Variable): 5000

Report Object Created
Report Identity Hash Code: 87654321
Report: Employee salary is 30000

Memory Concept:
Employee object -> Heap
Report object -> Heap
salary -> Instance field
bonus -> Local variable in display()
show() -> Executes in its own method frame
```

### Compile and Run

```bash
javac Employee.java
java Employee
```

### Concepts Demonstrated
- `salary` is an instance field belonging to an `Employee` object.
- `bonus` is a local variable declared inside `display()`.
- `Report` is a local inner class because it is declared inside a method.
- `Employee` and `Report` objects are normally allocated on the heap.
- Method calls use execution frames that hold method state and local variables; a frame is removed when its method returns.
- `System.identityHashCode()` returns an identity hash code, not a real memory address.

---

## Stack vs Heap — Quick Summary

| Item | Explanation |
|---|---|
| Stack frame | Holds method execution state and local variables/references |
| Heap | Memory area where Java objects are normally allocated |
| `age` | Instance field in a `Student` object |
| `marks` | Local variable in `Student.display()` |
| `salary` | Instance field in an `Employee` object |
| `bonus` | Local variable in `Employee.display()` |
| `Report` | Local inner class declared inside `Employee.display()` |
| Identity hash code | An object identity hash value, not the actual memory address |

## GitHub Submission Checklist

- [ ] Save the first program as `Student.java`.
- [ ] Save the second program as `Employee.java`.
- [ ] Compile and run both programs.
- [ ] Add this `README.md` file to the same repository.
- [ ] Upload all three files to GitHub.
- [ ] Submit the repository URL to the assignment website.

## Conclusion

Both programs demonstrate the relationship between Java objects, instance fields, local variables, method execution frames, and identity hash codes. The first program covers the basic concepts, while the second adds a local inner class and a second object.
