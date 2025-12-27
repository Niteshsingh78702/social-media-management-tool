package com.flintzy.socialtool.service;

import com.flintzy.socialtool.model.SocialAccount;
import com.flintzy.socialtool.model.User;
import com.flintzy.socialtool.payload.FacebookPagesResponse;
import com.flintzy.socialtool.repository.SocialAccountRepository;
import com.flintzy.socialtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacebookService {

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetch Facebook pages and save them to database
     */
    public List<SocialAccount> linkFacebookPages(String userEmail, String userAccessToken) {
        try {
            String url = "https://graph.facebook.com/v19.0/me/accounts?access_token=" + userAccessToken;
            
            FacebookPagesResponse response = restTemplate.getForObject(url, FacebookPagesResponse.class);

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new RuntimeException("No pages found or invalid token");
            }

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<SocialAccount> savedAccounts = new ArrayList<>();

            // Save or update each page
            for (FacebookPagesResponse.FacebookPage page : response.getData()) {
                SocialAccount account = socialAccountRepository.findByPageId(page.getId())
                        .orElse(new SocialAccount());

                account.setPageId(page.getId());
                account.setPageName(page.getName());
                account.setPageAccessToken(page.getAccessToken());
                account.setPlatform("FACEBOOK");
                account.setUser(user);

                savedAccounts.add(socialAccountRepository.save(account));
            }

            return savedAccounts;

        } catch (Exception e) {
            throw new RuntimeException("Failed to link Facebook pages: " + e.getMessage());
        }
    }

    /**
     * Publish a post to a Facebook page
     */
    public void publishPost(String pageId, String message, String userEmail) {
        try {
            // Find the social account
            SocialAccount account = socialAccountRepository.findByPageId(pageId)
                    .orElseThrow(() -> new RuntimeException("Page not found or not linked"));

            // Verify the page belongs to the user
            if (!account.getUser().getEmail().equals(userEmail)) {
                throw new RuntimeException("Unauthorized: This page doesn't belong to you");
            }

            // Publish to Facebook using URL parameters (simpler approach)
            String url = String.format(
                "https://graph.facebook.com/v19.0/%s/feed?message=%s&access_token=%s",
                pageId,
                message,
                account.getPageAccessToken()
            );

            restTemplate.postForEntity(url, null, String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish post: " + e.getMessage());
        }
    }

    /**
     * Get all linked pages for a user
     */
    public List<SocialAccount> getLinkedPages(String userEmail) {
        return socialAccountRepository.findByUserEmail(userEmail);
    }
}
