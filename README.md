# UG-SoftwareEngineeringIIProject-Group11
## Clean Architecture

![Expense Tracker Clean Architecture](docs/clean-architecture.png)

Development Strategy

	1. Member opens the user story they intend to start working on. 
	2. When the issue is clicked on, "Create a branch" under "Development" section of the issue gets selected. Main branch is protected.
	3. All work for the user story gets done in the branch. 
	4. At least any subtasks inside a commit are linked to the subtask via #IssueNr in the commit message.
	5. When work for the user story is done, the member creates a merge request and assigns reviewers such that members have equal amount of reviewing.
	6. At least 2 members will review and either approve or create a subtask for the user story stating what problem did they discover and notify the user story developer. If no faults discovered, merge with main is approved and the branch gets deleted automatically. 
	7. The developer moves the task to "Done" on the board.
	8. If some user story is missing from the board, the developer creates the issue "Usnr: desc" with the full description and acceptance criteria in the "Given/When/Then" format, gives it a priority and the label "user-story", and notifies others. 
