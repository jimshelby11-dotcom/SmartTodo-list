package app.controller;

import app.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;

public class MainController {

    @FXML private TextField titleField;
    @FXML private TextField categoryField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private TextArea descriptionArea;
    @FXML private TextField searchField;

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> categoryColumn;
    @FXML private TableColumn<Task, LocalDate> dueDateColumn;
    @FXML private TableColumn<Task, String> priorityColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, Boolean> completedColumn;

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private final String FILE_PATH = "tasks.txt";

    @FXML
    private void initialize() {

        priorityCombo.setItems(FXCollections.observableArrayList(
                "High", "Medium", "Low"
        ));

        titleColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        categoryColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory()));
        dueDateColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDueDate()));
        priorityColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPriority()));
        descriptionColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescription()));
        completedColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(c.getValue().isCompleted()));

        taskTable.setItems(taskList);

        loadTasksFromFile();
    }

    // Add Task
    @FXML
    private void onAddTask() {

        if (titleField.getText().isEmpty() || dueDatePicker.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please enter a title and due date.").show();
            return;
        }

        Task t = new Task(
                titleField.getText(),
                categoryField.getText(),
                dueDatePicker.getValue(),
                priorityCombo.getValue(),
                descriptionArea.getText()
        );

        taskList.add(t);
        saveTasksToFile();
        clearInputs();
    }

    private void clearInputs() {
        titleField.clear();
        categoryField.clear();
        descriptionArea.clear();
        dueDatePicker.setValue(null);
        priorityCombo.setValue(null);
    }

    // Delete Task
    @FXML
    private void onDeleteTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskList.remove(selected);
            saveTasksToFile();
        }
    }

    // Mark as completed
    @FXML
    private void onMarkCompleted() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setCompleted(true);
            taskTable.refresh();
            saveTasksToFile();
        }
    }

    // Edit Task
    @FXML
    private void onEditTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a task to edit.").show();
            return;
        }

        selected.setTitle(titleField.getText());
        selected.setCategory(categoryField.getText());
        selected.setDueDate(dueDatePicker.getValue());
        selected.setPriority(priorityCombo.getValue());
        selected.setDescription(descriptionArea.getText());

        taskTable.refresh();
        saveTasksToFile();
    }

    // Search
    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim().toLowerCase();

        ObservableList<Task> filtered = FXCollections.observableArrayList();

        for (Task t : taskList) {
            if (t.getTitle().toLowerCase().contains(keyword)
                    || t.getCategory().toLowerCase().contains(keyword)
                    || t.getDescription().toLowerCase().contains(keyword)) {

                filtered.add(t);
            }
        }

        taskTable.setItems(filtered);
    }

    // Clear Filter
    @FXML
    private void onClearFilter() {
        taskTable.setItems(taskList);
    }

    // Filter uncompleted tasks
    @FXML
    private void onFilterTasks() {
        ObservableList<Task> filtered = FXCollections.observableArrayList();

        for (Task t : taskList) {
            if (!t.isCompleted()) {
                filtered.add(t);
            }
        }

        taskTable.setItems(filtered);
    }

    @FXML
    private void onSaveTasks() {
        saveTasksToFile();
    }

    @FXML
    private void onLoadTasks() {
        taskList.clear();
        loadTasksFromFile();
    }

    @FXML
    private void onAbout() {
        new Alert(Alert.AlertType.INFORMATION,
                "Smart To-Do List\nCreated by JIN YANG").show();
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }

    private void saveTasksToFile() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task t : taskList) {
                w.write(t.toString());
                w.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",", -1);

                Task t = new Task(
                        p[0],
                        p[1],
                        LocalDate.parse(p[2]),
                        p[3],
                        p[4]
                );

                t.setCompleted(Boolean.parseBoolean(p[5]));
                taskList.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
