package com.example.keycloak.spi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedinProfile {

    private String vanityName;
    private ProfilePicture profilePicture;
    private String localizedFirstName;
    private String localizedLastName;
    private String localizedHeadline;

    @JsonProperty("vanityName")
    public String getVanityName() {
        return vanityName;
    }

    public void setVanityName(String vanityName) {
        this.vanityName = vanityName;
    }

    @JsonProperty("profilePicture")
    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    @JsonProperty("localizedFirstName")
    public String getLocalizedFirstName() {
        return localizedFirstName;
    }

    public void setLocalizedFirstName(String localizedFirstName) {
        this.localizedFirstName = localizedFirstName;
    }

    @JsonProperty("localizedLastName")
    public String getLocalizedLastName() {
        return localizedLastName;
    }

    public void setLocalizedLastName(String localizedLastName) {
        this.localizedLastName = localizedLastName;
    }

    @JsonProperty("localizedHeadline")
    public String getLocalizedHeadline() {
        return localizedHeadline;
    }

    public void setLocalizedHeadline(String localizedHeadline) {
        this.localizedHeadline = localizedHeadline;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProfilePicture {
        @JsonProperty("displayImage~")
        private DisplayImage displayImage;

        public DisplayImage getDisplayImage() {
            return displayImage;
        }

        public void setDisplayImage(DisplayImage displayImage) {
            this.displayImage = displayImage;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DisplayImage {
        private List<Element> elements;

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Element {
        private Data data;
        private List<Identifier> identifiers;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public List<Identifier> getIdentifiers() {
            return identifiers;
        }

        public void setIdentifiers(List<Identifier> identifiers) {
            this.identifiers = identifiers;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("com.linkedin.digitalmedia.mediaartifact.StillImage")
        private StillImage stillImage;

        public StillImage getStillImage() {
            return stillImage;
        }

        public void setStillImage(StillImage stillImage) {
            this.stillImage = stillImage;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StillImage {
        private DisplaySize displaySize;

        public DisplaySize getDisplaySize() {
            return displaySize;
        }

        public void setDisplaySize(DisplaySize displaySize) {
            this.displaySize = displaySize;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DisplaySize {
        private double width;

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identifier {
        private String identifier;
        private String identifierType;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifierType() {
            return identifierType;
        }

        public void setIdentifierType(String identifierType) {
            this.identifierType = identifierType;
        }
    }
}

