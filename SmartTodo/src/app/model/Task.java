package app.model;

import java.time.LocalDate;

public class Task {
    private String title;
    private String category;
    private LocalDate dueDate;
    private String priority;
    private String description;
    private boolean completed;

    public Task(String title, String category, LocalDate dueDate,
                String priority, String description) {
        this.title = title;
        this.category = category;
        this.dueDate = dueDate;
        this.priority = priority;
        this.description = description;
        this.completed = false;
    }

    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }

    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate d) { this.dueDate = d; }

    public String getPriority() { return priority; }
    public void setPriority(String p) { this.priority = p; }

    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean c) { this.completed = c; }

    @Override
    public String toString() {
        return title + "," + category + "," + dueDate + "," +
                priority + "," + description + "," + completed;
    }
}
