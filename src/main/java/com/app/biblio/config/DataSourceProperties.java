package com.app.biblio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    private BiblioDataSourceProperties biblio = new BiblioDataSourceProperties();
    private BiDataSourceProperties bi = new BiDataSourceProperties();

    public BiblioDataSourceProperties getBiblio() {
        return biblio;
    }

    public void setBiblio(BiblioDataSourceProperties biblio) {
        this.biblio = biblio;
    }

    public BiDataSourceProperties getBi() {
        return bi;
    }

    public void setBi(BiDataSourceProperties bi) {
        this.bi = bi;
    }

    public static class BiblioDataSourceProperties {
        private String url;
        private String username;
        private String password;
        private String driverClassName;

        // Getters and setters for each field
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }
    }

    public static class BiDataSourceProperties {
        private String url;
        private String username;
        private String password;
        private String driverClassName;

        // Getters and setters for each field
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }
    }
}
