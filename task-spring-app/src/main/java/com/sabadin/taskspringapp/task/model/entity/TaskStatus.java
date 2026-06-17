package com.sabadin.taskspringapp.task.model.entity;

import jakarta.annotation.Nonnull;

public enum TaskStatus {
    NEW("new"),
    WAIT("wait"),
    IN_PROGRESS("in_progress"),
    IN_TEST("in_test"),
    DONE("done"),
    REPORTED("reported"),
    ARCHIVE("archive");

    private final String title;

    TaskStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public static TaskStatus from(@Nonnull String title) {
        if (title == null) {
            return NEW;
        }
        for (TaskStatus value : values()) {
            if (value.getTitle().equalsIgnoreCase(title)) {
                return value;
            }
        }
        throw new IllegalArgumentException(title + " can not be converted to " + TaskStatus.class.getSimpleName());
    }
}
