package com.pksprojects.minis3.models.metadata;

import com.pksprojects.minis3.models.user.User;
import com.pksprojects.minis3.models.view.metadata.MetaDataView;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Created by PKS on 4/8/17.
 */
@Entity
public class MetaData {

    /** -- Properties -- */

    @Id
    private String Id;
    @NotNull
    private String fullPath;
    @NotNull
    private String name;
    @NotNull
    private Long size;
    @CreationTimestamp
    private LocalDateTime timeCreated;
    @UpdateTimestamp
    private LocalDateTime updated;
    @NotNull
    private String md5Hash;
    private String cacheControl;
    @NotNull
    private String contentDisposition;
    private String contentEncoding;
    private String contentLanguage;
    @NotNull
    private String contentType;
//    @OneToOne(cascade= CascadeType.ALL)
//    @MapKey(name = "name")
    @ElementCollection
    private Map<String, String> customMetaData;
    @ManyToOne
    private User user;

    /** -- Constructors -- */

    public MetaData() {
        Id = UUID.randomUUID().toString();
        timeCreated = LocalDateTime.now();
        updated = timeCreated;
    }

    public MetaData(Long size, String md5Hash, User user) {
        this();
        this.size = size;
        this.md5Hash = md5Hash;
        this.user = user;
    }

    public MetaData(String Id, Long size, String md5Hash, User user) {
        this(size, md5Hash, user);
        if(Id != null) this.Id = Id;
    }

    public MetaData(MetaDataView view, User user) {
        this(view.getId(), view.getSize(), view.getMd5Hash(), user);
        if(view.getTimeCreated() != null) timeCreated = view.getTimeCreated();
        if(view.getUpdated() != null) updated = view.getUpdated();
        name = view.getName();
        cacheControl = view.getCacheControl();
        contentDisposition = view.getContentDisposition();
        contentEncoding = view.getContentEncoding();
        contentLanguage = view.getContentLanguage();
        contentType = view.getContentType();
        customMetaData = view.getCustomMetaData();
    }

    /** -- Getters and Setters -- */

    public String getId() {
        return Id;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getCustomMetaData() {
        return customMetaData;
    }

    public void setCustomMetaData(Map<String, String> customMetaData) {
        this.customMetaData = customMetaData;
    }

    public User getUser() {
        return user;
    }
}
