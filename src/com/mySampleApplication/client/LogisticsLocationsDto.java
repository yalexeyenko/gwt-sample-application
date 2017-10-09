package com.mySampleApplication.client;

import java.util.Date;

public class LogisticsLocationsDto {

    private Date migrationDate;

    public LogisticsLocationsDto() {
        this.migrationDate = new Date();
    }

    public Date getMigrationDate() {
        return migrationDate;
    }

    public void setMigrationDate(Date migrationDate) {
        this.migrationDate = migrationDate;
    }
}
