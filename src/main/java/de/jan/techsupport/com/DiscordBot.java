package de.jan.techsupport.com;

import javax.security.auth.login.LoginException;

import de.jan.techsupport.nural.SupportClassifier;
import de.jan.techsupport.questions.Question;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class DiscordBot extends ListenerAdapter {
  String token;
  SupportClassifier classifier;
  JDA bot;

  public DiscordBot(SupportClassifier classifier, String token) {
    super();
    this.classifier = classifier;
    this.token = token;
  }
  
  public boolean start() {
    JDABuilder builder = JDABuilder.createDefault(token);
    try {
      bot = builder.build();
    } catch (LoginException e) {
      return false;
    }
    bot.addEventListener(this);
    return true;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent e) {
    MessageChannel channel = e.getChannel();
    User author = e.getAuthor();
    if(author.isBot()) return;
    if(channel.getType() != ChannelType.PRIVATE) return;
    String input = e.getMessage().getContentDisplay();
    Question question = classifier.classify(input);
    String response = question.getResponse(input);
    MessageAction action = channel.sendMessage(response);
    System.out.println("[Discord] "+author.getName()+" > "+input);
    System.out.println("[Discord] "+question.getTag()+" > "+response+"\n");
    action.submit();
  }

}