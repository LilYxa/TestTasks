package ru.ilia.fileShare.model;

import java.util.Objects;

public class FileData {

    private final String filePath;
    private volatile long lastAccessed;
    private final long uploadTime;

    public FileData(String filePath) {
        this.filePath = filePath;
        this.uploadTime = System.currentTimeMillis();
        this.lastAccessed = this.uploadTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setLastAccessed(long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileData fileData = (FileData) o;
        return lastAccessed == fileData.lastAccessed && uploadTime == fileData.uploadTime && Objects.equals(filePath, fileData.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, lastAccessed, uploadTime);
    }

    @Override
    public String toString() {
        return "FileData{" +
                "filePath='" + filePath + '\'' +
                ", lastAccessed=" + lastAccessed +
                ", uploadTime=" + uploadTime +
                '}';
    }
}
