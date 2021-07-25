package util;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.Locale;

public class Categories {
    public static String[] categories = {"general","books","film","music","musicals","television","video games","board games",
    "science & nature","computers","mathematics","mythology","sports","geography","history","politics","art","celebrities"
    ,"animals","vehicles","comics","gadgets","anime","cartoons"};

    public static String[] difficulties = {"easy","medium","hard"};

    public static String getLink(String link,String category) {
        int categoryIndex = 0;
        String difficulty = "";
        for (int i = 0; i < categories.length; i++) {
            String current = categories[i];
            if (category.toLowerCase(Locale.ROOT).equals(current)) {
                categoryIndex = i + 9;
            }
        }
        for (int i = 0; i < difficulties.length; i++) {
            String current = difficulties[i];
            if (category.toLowerCase(Locale.ROOT).equals(current)) {
                difficulty = current;
            }
        }
        if (categoryIndex != 0) {
            return link += "&category=" + categoryIndex;
        }
        if (!difficulty.equals(""))
        {
            return link += "&difficulty=" + difficulty;
        }
        return link;
    }

    public static EmbedBuilder getDifficulties() {
        String returning = "Difficulties - ";
        for(String string : difficulties)
        {
            returning += string + ",";
        }
        returning = returning.substring(0,returning.length()-1);
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.blue)
                .addField("Difficulties",returning)
                .setFooter("This bot was made by tridvajedan. https://github.com/tridvajedan");
        return builder;
    }

    public static EmbedBuilder getCategories() {
        String returning = "Categories - ";
        for(String string : categories)
        {
            returning += string + ",";
        }
        returning = returning.substring(0,returning.length()-1);
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.blue)
                .addField("Categories",returning)
                .setFooter("This bot was made by tridvajedan. https://github.com/tridvajedan");
        return builder;
    }
}
