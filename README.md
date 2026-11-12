# Project purpose and overview
This project is an expenses tracker application that helps users to keep track and have an overview of their expenses so that they have better insight into their spending habits and how to manage their money well.
The system allows the user to create and edit categories, add expenses to those categories or remove existing expenses, set budgets and see projected spending. The system takes into account the month and automatically resets when a new month starts, while keeping the categories of the previous month.

# Project structure

The project follows clean architecture and domain-driven design principles. 

### Bounded contexts with their ubiquitous language:
- **Budgeting bounded context** -- is concerned with the budgeting processes, such as adding expenses, categories and setting limits.
	- Expenses tracker - where the budgeting processes happen.
	- Basic user - person who performs the budgeting tasks inside the expenses tracker
	- Category - user can create categories to better delineate different expenses. Has a lifecycle.
	- Expense - Has a lifecycle and can be only added to an existing category.

### Tech Stack:
| Component | Technology |
|---|---|
| Frontend | React, Vite |
| Backend | Java 21, Spring Boot 4.1.1 |
| Database | PostgreSQL 18 |
| Build Tools | Maven |
| Testing | JUnit |
| Development Environment | Docker Compose |

### Backend:
Under src/main/java/ie/universityofgalway/group11/expensestracker/budgeting/ :
- application/ - packages for expense and category aggregate roots that contain corresponding use cases and command/response DTOs. Also has the package "common" that includes UseCase interface for uniform use case class creation, and "exceptions" package with custom domain exceptions.
- config/ - contains project configuration for Spring wiring/injection and web configuration for CORS.
- domain/ - center of the project, contains domain logic. Contains "category" and "expense" packages. Packages include classes "Category" and "Expense" that are the corresponding aggregate roots, and the associated custom value objects. The packages also include the repositories of the aggregate root with methods written in the domain language.
- infrastructure/ - is concerned with receiving, moving and returning data between backend, frontend and database. Includes 2 packages:
  	- persistance/ - concerned with persisting the data in database and transforming data from database entity to domain entity. includes database entities, JPA Repositories and persistance adapters, separated by domain entity packages for better distinction. Includes a root adapter helper class for entity transformation and InMemoryBudgetingRepository for initial testing.
  	- rest/ - Includes controllers and request DTOs for better data integrity and overview. These are also separated by domain entity packages for better distinction.
Under src/ :
- main/resources/ - application.properties for database connection and schema.sql for database initialisation with sample data.
- test/ - unit tests and integration tests.

### Frontend:
Under src/ :
- api/ - separate categoryApi.js and expenseApi.js
- components/ all .jsx files for different components used on page.
- components/contexts - contexts for data used between components
- App.jsx - main application. Has only one page.

### Clean architecture diagram
<img width="801" height="698" alt="image" src="https://github.com/user-attachments/assets/ba6a875e-e6f6-4786-9a23-582df9af4604" />

### UML Class diagram
<img width="10782" height="6848" alt="budgeting" src="https://github.com/user-attachments/assets/fffa5bae-86f0-422a-9844-00233b7f0461" />
(no exception classes and DTOs for slightly simpler-to-follow overview)


# Development Strategy

	1. Member opens the user story they intend to start working on. 
	2. When the issue is clicked on, "Create a branch" under "Development" section of the issue gets selected. Main branch is protected.
	3. All work for the user story gets done in the branch. 
	4. Issue or the subtask of an issue are linked inside the commit message via #IssueNr .
	5. When work for the user story is done, the member creates a merge request and assigns reviewers such that members have equal amount of reviewing.
	6. At least 1 member will review and either approve or create a subtask for the user story stating what problem did they discover and notify the user story developer. If no faults discovered, merge with main is approved and the branch gets deleted automatically. 
	7. The developer moves the task to "Done" on the board.
	8. If some user story is missing from the board, the developer creates the issue "USnr: desc" with the full description and acceptance criteria in the "Given/When/Then" format, gives it a priority and the label "user-story", and notifies others.
