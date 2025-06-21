package com.example.keycloak.spi;

import com.example.keycloak.spi.pojo.LinkedinProfile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.mappers.AbstractClaimMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.io.IOException;
import java.util.*;

/**
 * Identity Provider Mapper to fetch additional profile information from LinkedIn,
 * specifically the vanityName, headline, and the profile picture URL.
 */
public class LinkedinVanityNameMapper extends AbstractClaimMapper {

    private static final Logger logger = Logger.getLogger(LinkedinVanityNameMapper.class);

    public static final String PROVIDER_ID = "linkedin-vanity-name-mapper";
    private static final String LINKEDIN_API_URL =
            "https://api.linkedin.com/v2/me?projection=(id,vanityName,localizedFirstName,localizedLastName,localizedHeadline,profilePicture(displayImage~:playableStreams))";
    private static final String[] COMPATIBLE_PROVIDERS = {"linkedin", "linkedin-openid-connect"};

    static {
        logger.info("LinkedinVanityNameMapper class loaded");
    }

    @Override
    public String[] getCompatibleProviders() {
        return COMPATIBLE_PROVIDERS.clone();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayCategory() {
        return "Attribute Importer";
    }

    @Override
    public String getDisplayType() {
        return "LinkedIn Vanity Name Importer";
    }

    @Override
    public String getHelpText() {
        return "Fetches the vanityName, headline, and profilePicture from the LinkedIn API and stores them as user attributes.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return new ArrayList<>();
    }

    @Override
    public void importNewUser(KeycloakSession session, RealmModel realm, UserModel user,
                              IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        logger.debugf("importNewUser triggered for user: %s", context.getUsername());
        super.importNewUser(session, realm, user, mapperModel, context);
        fetchAndSetAttributes(session, user, context);
    }

    @Override
    public void updateBrokeredUser(KeycloakSession session, RealmModel realm, UserModel user,
                                   IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        logger.debugf("updateBrokeredUser triggered for user: %s", context.getUsername());
        super.updateBrokeredUser(session, realm, user, mapperModel, context);
        fetchAndSetAttributes(session, user, context);
    }

    private void fetchAndSetAttributes(KeycloakSession session, UserModel user, BrokeredIdentityContext context) {
        String rawToken = context.getToken();
        String accessToken = null;

        try {
            JsonNode node = new ObjectMapper().readTree(rawToken);
            accessToken = node.get("access_token").asText();
        } catch (Exception e) {
            logger.error("Failed to parse access_token from token JSON", e);
            return;
        }

        try {
            SimpleHttp.Response response = SimpleHttp.doGet(LINKEDIN_API_URL, session)
                    .header("Authorization", "Bearer " + accessToken)
                    .asResponse();

            if (response.getStatus() == 200) {
                LinkedinProfile profile = response.asJson(LinkedinProfile.class);

                if (profile.getVanityName() != null) {
                    user.setAttribute("vanityName", Collections.singletonList(profile.getVanityName()));
                    logger.debugf("Set vanityName [%s] for user: %s", profile.getVanityName(), user.getUsername());
                }

                if (profile.getLocalizedHeadline() != null) {
                    user.setAttribute("headline", Collections.singletonList(profile.getLocalizedHeadline()));
                    logger.debugf("Set headline [%s] for user: %s", profile.getLocalizedHeadline(), user.getUsername());
                }

                getHighestResolutionProfilePicture(profile).ifPresent(url -> {
                    user.setAttribute("profilePicture", Collections.singletonList(url));
                    logger.debugf("Set profilePicture for user: %s → %s", user.getUsername(), url);
                });

            } else {
                logger.errorf("Failed to fetch LinkedIn profile. Status: %d, Body: %s",
                        response.getStatus(), response.asString());
            }
        } catch (IOException e) {
            logger.error("Exception while fetching LinkedIn profile", e);
        }
    }

    private Optional<String> getHighestResolutionProfilePicture(LinkedinProfile profile) {
        if (profile.getProfilePicture() == null ||
                profile.getProfilePicture().getDisplayImage() == null ||
                profile.getProfilePicture().getDisplayImage().getElements() == null) {
            logger.info("No profile picture available for this LinkedIn user.");
            return Optional.empty();
        }

        return profile.getProfilePicture().getDisplayImage().getElements().stream()
                .filter(element -> element.getData() != null &&
                        element.getData().getStillImage() != null &&
                        element.getData().getStillImage().getDisplaySize() != null)
                .max(Comparator.comparingDouble(e -> e.getData().getStillImage().getDisplaySize().getWidth()))
                .flatMap(element -> element.getIdentifiers().stream()
                        .filter(id -> "EXTERNAL_URL".equals(id.getIdentifierType()))
                        .map(LinkedinProfile.Identifier::getIdentifier)
                        .findFirst());
    }
}

