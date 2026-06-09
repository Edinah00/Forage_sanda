package com.forage.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration racine de l'application (services, repositories, beans partagés).
 */
@Configuration
@ComponentScan(basePackages = { "com.forage.service", "com.forage.repository", "com.forage" })
public class RootConfig {
    // Beans globaux (DataSource, transactionManager, services) peuvent être ajoutés ici.
}
