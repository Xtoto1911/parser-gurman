package com.exemple;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String baseUrl = "https://shopgurman.ru/catalog.html";
        try {

            Document doc = getPage(baseUrl);

            Elements categories = doc.select("div.c-card-category");

            for (Element category : categories) {
                String nameCategory = category.select("a").text();
                String categoryUrl = category.select("a").attr("href");

                System.out.println("====================");
                System.out.println("Категория: " + nameCategory);
                System.out.println("====================");

                Document categoryPage = getPage(categoryUrl);
                Elements products = categoryPage.select("div.p-list__item");

                List<Product>  productList = new ArrayList<>();

                for (Element product : products) {
                    String productUrl = product.select("a").attr("href");
                    Document productPage = getPage(productUrl);

                    Product productInfo = parseProduct(productPage);
                    System.out.println(productInfo);
                    productList.add(productInfo);
                }

                saveProductsToCSV(productList, nameCategory);
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static Document getPage(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("http://www.google.com")
                .get();
    }

    private static Product parseProduct(Document doc) {
        String url = doc.location();

        String imageUrl = doc.select("div.p-page__image").select("a").attr("href");

        String price = doc.select("div.p-page__prices").text();
        if (price.isEmpty()) price = "Нет в наличии";

        String name = doc.select("div.p-page__descr__top-heading").text();
        String description = doc.select("div.p-page__description").text();

        return new Product(url, imageUrl, price, name, description);
    }

    private static void saveProductsToCSV(List<Product> products, String categotyName) {
        String safeName = Normalizer.normalize(categotyName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s]", "")
                .replace(" ", "_");

        String fileName = safeName + ".csv";

        try(FileWriter writer = new FileWriter(fileName)) {
            writer.write("Product URL;Image URL;Price;Name;Description\n");

            for (Product product : products) {
                writer.write(
                        "\"" + product.getUrl() + "\";" +
                                "\"" + product.getImageUrl() + "\";" +
                                "\"" + product.getPrice() + "\";" +
                                "\"" + product.getName().replace("\"", "'") + "\";" +
                                "\"" + product.getDescription().replace("\"", "'") + "\"\n"
                );
            }

            System.out.println("CSV сохранён: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка записи CSV: " + e.getMessage());
        }
    }
}
