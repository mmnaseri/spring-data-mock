package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.domain.Auditable;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:23 PM)
 */
@SuppressWarnings("unused")
public class AuditableEntity implements Auditable<String, String, Instant> {

    private String id;
    private String createdBy;
    private String lastModifiedBy;
    private Instant createdDate;
    private Instant lastModifiedDate;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @Nonnull
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(@Nonnull String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    @Nonnull
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(@Nonnull String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    @Nonnull
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    @Override
    public void setCreatedDate(@Nonnull Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    @Nonnull
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(@Nonnull Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
