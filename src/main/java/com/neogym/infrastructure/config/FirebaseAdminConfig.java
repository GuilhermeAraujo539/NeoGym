
package com.neogym.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class FirebaseAdminConfig {

    private static final String APP_NAME = "neogym";

    @Bean
    public FirebaseApp firebaseApp(
            @Value("${neogym.firebase.project-id}") String projectId
    ) throws IOException {

        List<FirebaseApp> apps = FirebaseApp.getApps();

        for (FirebaseApp app : apps) {
            if (APP_NAME.equals(app.getName())) {
                return app;
            }
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(
                        GoogleCredentials.getApplicationDefault()
                )
                .setProjectId(projectId)
                .build();

        return FirebaseApp.initializeApp(options, APP_NAME);
    }

    @Bean
    public FirebaseAuth firebaseAdminAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
