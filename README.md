# Keycloak LinkedIn SPI Enricher

This Keycloak Service Provider Interface (SPI) extension enriches user information retrieved from LinkedIn by fetching additional fields such as:

- `vanityName`
- `headline` (LinkedIn title)
- High-resolution `profilePicture` URLs

This is useful when using **LinkedIn as an Identity Provider** (IdP) in Keycloak and you need more than just the default `email`, `firstName`, and `lastName`.

---

## Features

Fetches and stores:

- `vanityName` as a custom user attribute  
- `headline` as a custom user attribute  
- `profilePicture` in multiple resolutions as custom user attributes

Automatically works on **first login** and **user sync**

Fully compatible with Keycloak 22–26+

---
## Guide

For a full walkthrough of setting up this SPI and integrating LinkedIn with Keycloak, read:

🔗 [Enriching Keycloak with LinkedIn VanityName, Headline, and Profile Picture via Custom SPI](https://dev.to/maradwan/enriching-keycloak-with-linkedin-vanityname-headline-profile-picture-via-custom-spi-g40)

---

## Requirements

- **Keycloak version:** 22 or later (tested on 26.2.5)
- **LinkedIn Developer Account**
- **Custom Realm set up in Keycloak**

---

## Installation

1. **Clone the Repo & Build the JAR**

```bash
git clone https://github.com/YOUR_USERNAME/keycloak-linkedin-spi.git
cd keycloak-linkedin-spi
mvn clean package

### Version 2.0.0

This version introduces support for setting the Keycloak `username` using the LinkedIn `vanityName`.

- `username` will now be automatically set on user creation. 
- `vanityName` is still available as a custom user attribute.

Make sure Edit username is On in User info settings, you will find it in Realm settings --> Login

