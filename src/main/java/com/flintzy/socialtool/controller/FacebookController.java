package com.flintzy.socialtool.controller;

import com.flintzy.socialtool.model.SocialAccount;
import com.flintzy.socialtool.payload.ApiResponse;
import com.flintzy.socialtool.payload.LinkPagesRequest;
import com.flintzy.socialtool.payload.PostRequest;
import com.flintzy.socialtool.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facebook")
public class FacebookController {

    @Autowired
    private FacebookService facebookService;

    /**
     * Link Facebook pages for the authenticated user
     * POST /api/facebook/link-pages
     */
    @PostMapping("/link-pages")
    public ResponseEntity<?> linkPages(@RequestBody LinkPagesRequest request,
                                       Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<SocialAccount> linkedPages = facebookService.linkFacebookPages(userEmail, request.getUserAccessToken());
            
            return ResponseEntity.ok(new ApiResponse(true, 
                    linkedPages.size() + " Facebook page(s) linked successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    /**
     * Get all linked Facebook pages for the authenticated user
     * GET /api/facebook/pages
     */
    @GetMapping("/pages")
    public ResponseEntity<?> getLinkedPages(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<SocialAccount> pages = facebookService.getLinkedPages(userEmail);
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    /**
     * Publish a post to a Facebook page
     * POST /api/facebook/post
     */
    @PostMapping("/post")
    public ResponseEntity<?> publishPost(@RequestBody PostRequest request,
                                         Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            facebookService.publishPost(request.getPageId(), request.getMessage(), userEmail);
            
            return ResponseEntity.ok(new ApiResponse(true, "Post published successfully on Facebook"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
