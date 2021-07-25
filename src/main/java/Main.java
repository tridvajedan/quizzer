import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommand;
import org.json.JSONObject;
import util.Categories;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        String token = "ODY4NDYyNTQ3Mzc0ODUwMDc4.YPwAxg.7iCvZAzV6-OX-kBrI293WBhcVuQ";
        DiscordApi botApi = new DiscordApiBuilder().setToken(token).login().join();
        String tempLink = botApi.createBotInvite();
        String inviteLink = tempLink.substring(0,tempLink.length()-1)+"2415950848";
        System.out.println(inviteLink);
        SlashCommand command = SlashCommand.with("trivia","Get a random trivia question and play!").createGlobal(botApi).join();
        botApi.addMessageCreateListener(event ->
        {
            if (event.getMessageContent().charAt(0) == 'q') {
                 if (event.getMessageContent().substring(2).equals("categories")) {
                    event.getChannel().sendMessage(Categories.getCategories());
                }
                 else if (event.getMessageContent().substring(2).equals("help")) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.red)
                            .addField("Prefix", "q")
                            .addField("Commands", "`trivia` `trivia (category)` `trivia (difficulty)` `help` `categories` `difficulties`")
                            .addField("Wanna invite me to your server? Here's the link!","[Invite me!]("+inviteLink+")");
                    event.getChannel().sendMessage(builder);
                }
                 else if(event.getMessageContent().substring(2).equals("difficulties"))
                 {
                     event.getChannel().sendMessage(Categories.getDifficulties());
                 }
                else if (event.getMessageContent().substring(2).contains("trivia")) {
                        String link = "https://opentdb.com/api.php?amount=1";
                        try {
                            if (!event.getMessageContent().substring(9).equals("")) {
                                link = Categories.getLink(link, event.getMessageContent().substring(9));
                            }
                        }
                        catch (StringIndexOutOfBoundsException e)
                        {

                        }
                        Question question = jsonGetRequest(link);
                        MessageBuilder builder = new MessageBuilder()
                                .setEmbed(new EmbedBuilder()
                                        .setAuthor(question.question)
                                        .setColor(Color.YELLOW)
                                        .addField("Category", question.category)
                                        .addField("Type", question.type)
                                        .setFooter("This bot was made by tridvajedan. https://github.com/tridvajedan")
                                        .addField("Difficulty", question.difficulty));
                        ArrayList<LowLevelComponent> buttons = new ArrayList<>();
                        Random r = new Random();
                        int correct = r.nextInt(4);
                        int skipped = 0;
                        for (int i = 0; i < question.wrongAnswers.length + 1; i++) {
                            if (i == correct) {
                                skipped = -1;
                                buttons.add(Button.primary("correct", question.correctAnswer));
                                continue;
                            } else {
                                buttons.add(Button.primary("incorrect", question.wrongAnswers[i + skipped]));
                            }
                        }
                        builder.addComponents(ActionRow.of(buttons));
                        builder.send(event.getChannel());
                    }
                else
                 {
                     EmbedBuilder builder = new EmbedBuilder()
                             .setColor(Color.red)
                             .setAuthor("Well that's awkward...")
                             .setFooter("This bot was made by tridvajedan. https://github.com/tridvajedan")
                             .setTitle("You used our prefix, but we cant find that command! Use q help.");
                     event.getChannel().sendMessage(builder);
                 }
        }
        });

        botApi.addMessageComponentCreateListener(event -> {
            User clicker = event.getMessageComponentInteraction().getUser();

            MessageComponentInteraction interaction = event.getMessageComponentInteraction();
            interaction.getMessage().get().delete();
            String id = interaction.getCustomId();
            switch (id)
            {
                default:
                    interaction.createImmediateResponder().setContent("Wrong! Better luck next time " + clicker.getMentionTag() + "!").respond();
                    break;
                case "correct":
                    interaction.createImmediateResponder().setContent("Correct! Nice guess " + clicker.getMentionTag() + "!").respond();
            }
        });
    }

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public static Question jsonGetRequest(String urlQueryString) {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            json = streamToString(inStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JSONObject result = new JSONObject(json).getJSONArray("results").getJSONObject(0);
        Question question = new Question(result.getString("category"),result.getString("type"),result.getString("difficulty"),result.getString("question"),result.getString("correct_answer"),result.getJSONArray("incorrect_answers").toString().split(","));
        return question;
    }
}
