package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platforms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Platform extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 500)
    private String baseUrl;

    @Column(length = 500)
    private String logoUrl;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean scrapable = true;

    @Column(columnDefinition = "TEXT")
    private String scraperConfigJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlatformType type = PlatformType.MARKETPLACE;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isScrapable() { return scrapable; }
    public void setScrapable(boolean scrapable) { this.scrapable = scrapable; }
    public String getScraperConfigJson() { return scraperConfigJson; }
    public void setScraperConfigJson(String scraperConfigJson) { this.scraperConfigJson = scraperConfigJson; }
    public PlatformType getType() { return type; }
    public void setType(PlatformType type) { this.type = type; }

    public static PlatformBuilder builder() { return new PlatformBuilder(); }
    public static class PlatformBuilder {
        private String name;
        private String code;
        private String baseUrl;
        private String logoUrl;
        private boolean active = true;
        private boolean scrapable = true;
        private String scraperConfigJson;
        private PlatformType type = PlatformType.MARKETPLACE;

        public PlatformBuilder name(String name) { this.name = name; return this; }
        public PlatformBuilder code(String code) { this.code = code; return this; }
        public PlatformBuilder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public PlatformBuilder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public PlatformBuilder active(boolean active) { this.active = active; return this; }
        public PlatformBuilder scrapable(boolean scrapable) { this.scrapable = scrapable; return this; }
        public PlatformBuilder scraperConfigJson(String scraperConfigJson) { this.scraperConfigJson = scraperConfigJson; return this; }
        public PlatformBuilder type(PlatformType type) { this.type = type; return this; }

        public Platform build() {
            Platform p = new Platform();
            p.setName(name);
            p.setCode(code);
            p.setBaseUrl(baseUrl);
            p.setLogoUrl(logoUrl);
            p.setActive(active);
            p.setScrapable(scrapable);
            p.setScraperConfigJson(scraperConfigJson);
            p.setType(type != null ? type : PlatformType.MARKETPLACE);
            return p;
        }
    }

    public enum PlatformType {
        MARKETPLACE, BRAND_STORE, AGGREGATOR
    }
}