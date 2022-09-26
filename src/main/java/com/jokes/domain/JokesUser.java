package com.jokes.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A JokesUser.
 */
@Entity
@Table(name = "jokes_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JokesUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_joke", nullable = false)
    private String idJoke;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JokesUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdJoke() {
        return this.idJoke;
    }

    public JokesUser idJoke(String idJoke) {
        this.setIdJoke(idJoke);
        return this;
    }

    public void setIdJoke(String idJoke) {
        this.idJoke = idJoke;
    }

    public String getUrl() {
        return this.url;
    }

    public JokesUser url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public JokesUser iconUrl(String iconUrl) {
        this.setIconUrl(iconUrl);
        return this;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getValue() {
        return this.value;
    }

    public JokesUser value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public JokesUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JokesUser)) {
            return false;
        }
        return id != null && id.equals(((JokesUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JokesUser{" +
            "id=" + getId() +
            ", idJoke='" + getIdJoke() + "'" +
            ", url='" + getUrl() + "'" +
            ", iconUrl='" + getIconUrl() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
