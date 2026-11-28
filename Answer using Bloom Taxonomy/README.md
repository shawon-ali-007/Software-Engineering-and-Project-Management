# Software Design, Architecture, Design Patterns & UI Design

## Exam-Ready Answers --- 40 Marks

**Writing target:** These answers are kept concise and mark-oriented so
they can be written within approximately **1 hour**. Each question is
followed by its answer.

------------------------------------------------------------------------

# Question 1: Design Fundamentals & Object-Oriented Quality Metrics \[14 Marks\]

## (a) \[4 Marks\] --- \[CO1 \| Bloom's: Knowledge (C1)\]

**Question:** State the **two** basic steps of the software design
process and identify the key output artifacts (including why design
rationale is recorded) produced at the end of the design phase.

**Answer:**

The two basic steps of the software design process are:

1.  **Architectural design:** Defines the overall structure of the
    system, its major components or subsystems, and how they
    communicate.
2.  **Detailed design:** Describes the internal design of each
    component, including classes, methods, data structures, interfaces,
    and algorithms.

**Key design outputs (artifacts):**

-   **Architecture/design model:** Shows the system structure and
    relationships among components.
-   **Interface specifications:** Define how components communicate with
    one another.
-   **Component or class design:** Describes classes, methods, data, and
    responsibilities.
-   **Data design:** Defines data structures and database organization.
-   **Design rationale:** Records why particular design decisions were
    made, including alternatives and trade-offs. It helps future
    developers understand, maintain, and modify the system.

------------------------------------------------------------------------

## (b) \[5 Marks\] --- \[CO1 \| Bloom's: Understanding (C2)\]

**Question:** Explain why software engineers aim for **low coupling**
and **high cohesion** when decomposing a system into modules.
Distinguish clearly between *data coupling* and *control coupling*, and
between *functional cohesion* and *coincidental cohesion*.

**Answer:**

Software engineers aim for **low coupling** and **high cohesion** to
make a system easier to understand, test, maintain, and modify.

-   **Low coupling** means modules have minimum dependency on one
    another. A change in one module is less likely to affect other
    modules.
-   **High cohesion** means the elements inside a module work together
    to perform one clear, related task. It makes the module focused and
    easier to maintain.

### Data Coupling vs. Control Coupling

-   **Data coupling:** Two modules communicate by passing only the data
    they need through parameters. It is generally desirable because
    dependency is limited.
-   **Control coupling:** One module passes control information, such as
    a flag or command, that determines the internal logic or execution
    path of another module. It creates stronger dependency.

### Functional Cohesion vs. Coincidental Cohesion

-   **Functional cohesion:** Every element of a module contributes to
    one well-defined function. It is the strongest and most desirable
    form of cohesion.
-   **Coincidental cohesion:** Unrelated tasks are placed together in
    the same module without a meaningful relationship. It is weak and
    makes the module difficult to understand and maintain.

Therefore, good software design tries to achieve **low coupling and high
cohesion**.

------------------------------------------------------------------------

## (c) \[5 Marks\] --- \[CO2 \| Bloom's: Understanding (C2)\]

**Question:** Explain what the **Lack of Cohesion of Methods (LCOM2 and
LCOM3)** metrics measure in an object-oriented class. Interpret what a
value near `0` indicates, and explain conceptually why an **LCOM3**
value greater than `1` signals the presence of *dead attributes*.

**Answer:**

LCOM metrics measure how strongly the methods of a class are related
through the instance attributes they use. They help identify whether a
class has one focused responsibility or contains unrelated
responsibilities.

-   **LCOM2:** Measures the difference between pairs of methods that do
    not share attributes and pairs of methods that share attributes. A
    value near **0** indicates that methods are well connected through
    common attributes and the class has good cohesion.
-   **LCOM3:** Measures the lack of cohesion by comparing the number of
    methods with the number of attributes and the connections between
    them. A value near **0** indicates high cohesion, while a larger
    value indicates lower cohesion.

**Why can LCOM3 greater than 1 indicate dead attributes?**

Conceptually, an attribute is useful when it is accessed by at least one
method. If an attribute is not used by any method, it is a **dead
attribute**. Such an attribute increases the total attribute count but
does not create any connection between methods. This can make the LCOM3
value exceed 1, indicating unused attributes and poor class design.

------------------------------------------------------------------------

# Question 2: Software Architecture & Design Patterns \[18 Marks\]

## (a) \[4 Marks\] --- \[CO3 \| Bloom's: Knowledge (C1)\]

**Question:** List any **four** software architectural styles covered in
the course (from *Layered*, *Event-Based*, *Model--View--Controller*,
*Blackboard/Repository*, and *Pipes and Filters*) and state the primary
structural components of each.

**Answer:**

1.  **Layered Architecture**
    -   Components: A set of layers, such as presentation, business
        logic, and data-access layers.
    -   Each layer provides services to the layer above it.
2.  **Event-Based Architecture**
    -   Components: Event producers, events, an event channel or event
        bus, and event consumers/handlers.
    -   Producers publish events, and interested consumers respond to
        them.
3.  **Model--View--Controller (MVC)**
    -   Components: Model, View, and Controller.
    -   Model manages data, View displays information, and Controller
        handles user input.
4.  **Pipes and Filters Architecture**
    -   Components: Filters and pipes.
    -   Filters process data, while pipes transfer the output of one
        filter to the next.

------------------------------------------------------------------------

## (b) \[5 Marks\] --- \[CO3 \| Bloom's: Understanding (C2)\]

**Question:** Compare the **Event-Based** architectural style with the
**Pipes and Filters** architectural style, explaining how components
interact, share data, and maintain independence in each style.

**Answer:**

Both architectures divide a system into independent components, but they
differ in how information flows.

  -----------------------------------------------------------------------
  Aspect                  Event-Based             Pipes and Filters
  ----------------------- ----------------------- -----------------------
  Interaction             Components communicate  Filters communicate
                          by publishing and       through pipes.
                          receiving events.       

  Data sharing            Events carry            Each filter receives
                          information about       data, processes it, and
                          something that has      passes the result
                          happened.               forward.

  Flow                    Usually asynchronous    Usually a sequence or
                          and event-driven.       pipeline of processing
                                                  steps.

  Independence            Producers do not need   Filters do not need to
                          to know the consumers.  know the internal
                                                  implementation of other
                                                  filters.
  -----------------------------------------------------------------------

**Explanation:**

In **Event-Based architecture**, an event producer publishes an event to
an event channel or bus. Interested consumers receive the event and
perform their tasks. The producer and consumer remain independent
because they do not directly call each other.

In **Pipes and Filters architecture**, data moves through a series of
filters. Each filter performs a specific transformation and sends its
output through a pipe to the next filter. Filters remain independent
because they communicate through defined data interfaces.

Thus, Event-Based architecture is suitable for event-driven reactions,
while Pipes and Filters is suitable for step-by-step data processing.

------------------------------------------------------------------------

## (c) \[5 Marks\] --- \[CO4 \| Bloom's: Understanding (C2)\]

**Question:** Explain the structural difference between the **Strategy**
pattern and the **Template Method** pattern in handling algorithmic
variations, and describe **one benefit** and **one liability** of each.

**Answer:**

The **Strategy** and **Template Method** patterns both support
algorithmic variation, but they use different structures.

### Strategy Pattern

-   It defines a family of algorithms in separate classes that implement
    a common interface.
-   A context object uses a selected strategy and can change the
    algorithm at runtime.

**Benefit:** Algorithms can be changed at runtime without modifying the
context class.

**Liability:** It may create many small strategy classes and require
additional object management.

### Template Method Pattern

-   It defines the overall algorithm structure in a superclass method.
-   Some steps are implemented or overridden by subclasses, while the
    main algorithm sequence remains fixed.

**Benefit:** It reuses common algorithm structure and avoids duplicating
the overall process.

**Liability:** It depends on inheritance, so changing behavior at
runtime is less flexible and subclasses may become tightly coupled to
the superclass.

**Main difference:** Strategy uses **composition and delegation**,
whereas Template Method uses **inheritance and method overriding**.

------------------------------------------------------------------------

## (d) \[4 Marks\] --- \[CO4 \| Bloom's: Understanding (C2)\]

**Question:** When mapping an object-oriented inheritance hierarchy to a
relational database, explain how **Single Table Inheritance** differs
from **Class Table Inheritance**, highlighting one key trade-off (such
as query joins, storage space, or schema refactoring impact) for each.

**Answer:**

### Single Table Inheritance (STI)

-   The entire inheritance hierarchy is stored in one database table.
-   A type or discriminator column identifies the specific subclass of
    each row.
-   **Trade-off:** Queries are simple and usually require fewer joins,
    but the table may contain many nullable columns because different
    subclasses have different attributes.

### Class Table Inheritance (CTI)

-   A separate table is created for the superclass and for each
    subclass.
-   The subclass table stores subclass-specific attributes and links to
    the superclass table through a primary/foreign key.
-   **Trade-off:** The database structure is more normalized and avoids
    many unused columns, but retrieving a complete subclass object often
    requires joins.

Therefore, STI favors simpler queries, while CTI favors a more
normalized table structure.

------------------------------------------------------------------------

# Question 3: User Interface (UI) Design & Accessibility \[8 Marks\]

## (a) \[4 Marks\] --- \[CO5 \| Bloom's: Knowledge (C1)\]

**Question:** List **four** fundamental UI design principles (e.g.,
*learnability*, *user familiarity*, *consistency*, *minimal surprise*,
*recoverability*, *user guidance*, *user diversity*) and identify
**two** standard UI interaction modalities.

**Answer:**

**Four fundamental UI design principles:**

1.  **Learnability:** The interface should be easy for new users to
    learn.
2.  **Consistency:** Similar actions and elements should behave in
    similar ways throughout the system.
3.  **Recoverability:** Users should be able to recover from errors, for
    example through undo, redo, or clear error messages.
4.  **User guidance:** The interface should provide helpful
    instructions, feedback, and prompts when needed.

**Two standard UI interaction modalities:**

-   **Command-line interface (CLI):** Users interact by typing commands.
-   **Menu-based interface:** Users interact by selecting options from
    displayed menus.

------------------------------------------------------------------------

## (b) \[4 Marks\] --- \[CO5 \| Bloom's: Understanding (C2)\]

**Question:** Explain how a user interface designer should use color and
visual cues to present information effectively to users with **color
vision deficiencies (color blindness)**, including why relying on color
alone or red--green combinations should be avoided.

**Answer:**

A UI designer should make the interface understandable even for users
who cannot distinguish certain colors.

1.  **Do not rely on color alone:** Use text labels, icons, shapes,
    patterns, or symbols along with color. For example, show a warning
    icon and the word "Error" instead of using only a red message.
2.  **Avoid red--green combinations:** Many users with color vision
    deficiencies have difficulty distinguishing red from green. Use
    combinations with sufficient contrast, such as blue and orange.
3.  **Maintain sufficient contrast:** Text and important interface
    elements should have clear contrast against their backgrounds.
4.  **Use consistent visual cues:** Apply clear labels, icons, borders,
    and patterns so users can identify different states without
    depending only on color.

These practices improve accessibility and make the interface easier for
everyone to use.

------------------------------------------------------------------------

## Quick Revision Checklist

-   **Q1:** Architectural design, detailed design, design artifacts,
    rationale, coupling, cohesion, LCOM2, LCOM3.
-   **Q2:** Four architectural styles, Event-Based vs Pipes and Filters,
    Strategy vs Template Method, STI vs CTI.
-   **Q3:** Four UI principles, two interaction modalities, accessible
    color and visual cues.

**Exam tip:** Keep definitions precise, use the comparison tables where
appropriate, and underline key terms such as *low coupling*, *high
cohesion*, *composition*, *inheritance*, *STI*, and *CTI*.
