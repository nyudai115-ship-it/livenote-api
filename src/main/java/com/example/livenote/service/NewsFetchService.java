package com.example.livenote.service;

import com.example.livenote.entity.News;
import com.example.livenote.repository.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import java.net.URL;
import java.util.List;

@Service
public class NewsFetchService {
    // DB保存せず外部APIから直接取得
    // ※旧fetchNewsDirect(String artistName)は削除

    public List<News> fetchNewsDirect(String artistName, int page) {
        List<News> result = new java.util.ArrayList<>();
        int pageSize = 20;
        String[] queries;
        if ("jpop".equalsIgnoreCase(artistName)) {
            queries = new String[] { "jpop" };
        } else if ("kpop".equalsIgnoreCase(artistName)) {
            queries = new String[] { "kpop" };
        } else {
            queries = new String[] { "jpop", "kpop" };
        }
        for (String query : queries) {
            try {
                String url = "https://newsapi.org/v2/everything?q=" + query + "&apiKey=" + newsApiKey + "&pageSize="
                        + pageSize + "&page=" + page;
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Connection", "close");
                try (java.io.InputStream is = conn.getInputStream();
                        java.util.Scanner s = new java.util.Scanner(
                                new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))
                                .useDelimiter("\\A")) {
                    String json = s.hasNext() ? s.next() : "";
                    com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
                    for (com.google.gson.JsonElement elem : obj.getAsJsonArray("articles")) {
                        com.google.gson.JsonObject article = elem.getAsJsonObject();
                        News news = new News();
                        news.setTitle(article.get("title").getAsString());
                        news.setUrl(article.get("url").getAsString());
                        if (article.has("urlToImage") && !article.get("urlToImage").isJsonNull()) {
                            news.setArtistName(article.get("urlToImage").getAsString());
                        } else {
                            news.setArtistName("");
                        }
                        news.setDate(new java.util.Date());
                        result.add(news);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    // 10分ごとにお気に入りアーティストのニュースを自動取得
    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 600000)
    public void autoFetchNewsForFavorites() {
        // 全ユーザーのお気に入りアーティスト名を取得
        java.util.List<com.example.livenote.entity.FavoriteArtist> favorites = favoriteArtistRepository.findAll();
        java.util.Set<String> artistNames = new java.util.HashSet<>();
        for (com.example.livenote.entity.FavoriteArtist fav : favorites) {
            if (fav.getArtist() != null && fav.getArtist().getName() != null) {
                artistNames.add(fav.getArtist().getName());
            }
        }
        for (String name : artistNames) {
            fetchNewsApiOrg(name);
            fetchBingNews(name);
        }
    }

    @org.springframework.beans.factory.annotation.Value("${newsapi.api-key}")
    private String newsApiKey;

    // NewsAPI.org連携（無料枠あり）
    public void fetchNewsApiOrg(String artistName) {
        // TODO: 必要ならLoggerで出力
        try {
            String url = "https://newsapi.org/v2/everything?q=" + artistName + "&language=ja&sortBy=publishedAt&apiKey="
                    + newsApiKey;
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("GET");
            java.io.InputStream is = conn.getInputStream();
            try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
                String json = s.hasNext() ? s.next() : "";
                com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
                for (com.google.gson.JsonElement elem : obj.getAsJsonArray("articles")) {
                    com.google.gson.JsonObject article = elem.getAsJsonObject();
                    News news = new News();
                    news.setTitle(article.get("title").getAsString());
                    news.setUrl(article.get("url").getAsString());
                    news.setDate(new java.util.Date());
                    news.setArtistName(artistName);
                    newsRepository.save(news);
                }
            }
        } catch (Exception e) {
            // ログ出力のみ
        }
    }

    // Bing News Search API連携（無料枠あり）
    public void fetchBingNews(String artistName) {
        try {
            String url = "https://api.bing.microsoft.com/v7.0/news/search?q=" + artistName + "&mkt=ja-JP";
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("GET");
            java.io.InputStream is = conn.getInputStream();
            try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
                String json = s.hasNext() ? s.next() : "";
                com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
                for (com.google.gson.JsonElement elem : obj.getAsJsonArray("value")) {
                    com.google.gson.JsonObject article = elem.getAsJsonObject();
                    News news = new News();
                    news.setTitle(article.get("name").getAsString());
                    news.setUrl(article.get("url").getAsString());
                    news.setDate(new java.util.Date());
                    news.setArtistName(artistName);
                    newsRepository.save(news);
                }
            }
        } catch (Exception e) {
            // ログ出力のみ
        }
    }

    private final NewsRepository newsRepository;
    private final com.example.livenote.repository.FavoriteArtistRepository favoriteArtistRepository;
    // 自動取得ON/OFFフラグ（trueで有効）
    private volatile boolean autoFetchEnabled = true;

    public NewsFetchService(NewsRepository newsRepository,
            com.example.livenote.repository.FavoriteArtistRepository favoriteArtistRepository) {
        this.newsRepository = newsRepository;
        this.favoriteArtistRepository = favoriteArtistRepository;
    }

    public void setAutoFetchEnabled(boolean enabled) {
        this.autoFetchEnabled = enabled;
    }

    public boolean isAutoFetchEnabled() {
        // Example: Just return the flag value, remove the invalid code using
        // artistNames
        return autoFetchEnabled;
    }

    // Google News RSS例（アーティスト名で検索）
    // cron式で柔軟に自動取得（例: 毎日6時, 12時, 18時, 23時）
    @Scheduled(cron = "0 0 6,12,18,23 * * *")
    public void fetchGoogleNewsRss() {
        List<com.example.livenote.entity.FavoriteArtist> favorites = favoriteArtistRepository.findAll();
        List<String> artistNames = favorites.stream()
                .map(fav -> fav.getArtist().getName())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        for (String artist : artistNames) {
            try {
                String rssUrl = "https://news.google.com/rss/search?q=" + artist;
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(new URL(rssUrl)));
                for (SyndEntry entry : feed.getEntries()) {
                    News news = new News();
                    news.setTitle(entry.getTitle());
                    news.setUrl(entry.getLink());
                    news.setDate(new java.util.Date());
                    news.setArtistName(artist);
                    newsRepository.save(news);
                }
            } catch (Exception e) {
                // ログ出力のみ
            }
        }
    }
}
