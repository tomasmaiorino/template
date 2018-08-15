package com.tsm.template.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseModel {

    @Getter
    private LocalDateTime created;

    @Getter
    private LocalDateTime lastUpdated;

    @JsonIgnore
    public boolean isNew() {
        return created == null;
    }

    @PrePersist
    public void setCreated() {
    	created = LocalDateTime.now();
    }

    @PreUpdate
    public void setLastUpdated() {
    	lastUpdated= LocalDateTime.now();
    }

    public Class getStatusEnum() {
        return null;
    }

}
