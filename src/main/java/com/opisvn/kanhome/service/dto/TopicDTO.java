package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Topic entity.
 */
public class TopicDTO implements Serializable {

    private String id;

    private String name;

    private String topics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TopicDTO topicDTO = (TopicDTO) o;
        if(topicDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), topicDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TopicDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", topics='" + getTopics() + "'" +
            "}";
    }
}
