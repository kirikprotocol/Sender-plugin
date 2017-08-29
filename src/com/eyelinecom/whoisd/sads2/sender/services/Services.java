package com.eyelinecom.whoisd.sads2.sender.services;

import com.eyeline.utils.config.xml.XmlConfigSection;
import com.eyelinecom.whoisd.sads2.sender.services.i18n.MessageProvider;
import com.eyelinecom.whoisd.sads2.sender.services.messaging.FeedbackProvider;
import com.eyelinecom.whoisd.sads2.sender.services.messaging.FeedbackService;
import com.eyelinecom.whoisd.sads2.sender.services.sender.SenderProvider;
import com.eyelinecom.whoisd.sads2.sender.services.sender.SenderService;

public class Services {

  private final SenderProvider senderProvider;
  private final FeedbackProvider feedbackProvider;
  private final MessageProvider messageProvider;

  public Services(XmlConfigSection config) throws ServicesException {
    this.messageProvider = new MessageProvider();
    this.senderProvider = initSenderProvider(config);
    this.feedbackProvider = initFeedbackProvider(config, messageProvider);
  }

  private static SenderProvider initSenderProvider(XmlConfigSection config) throws ServicesException {
    try {
      String pushApiUrl = config.getString("push.api.url");
      String profileStorageApiUrl = config.getString("profile.storage.api.url");
      XmlConfigSection apiClientSection = config.getSection("api.client");
      int connectTimeout = apiClientSection.getInt("api.client.connectTimeout.millis");
      int requestTimeout = apiClientSection.getInt("api.client.requestTimeout.millis");
      return new SenderService(profileStorageApiUrl, pushApiUrl, connectTimeout, requestTimeout);
    }
    catch (Exception e) {
      throw new ServicesException("Error during SenderProvider initialization.", e);
    }
  }

  private static FeedbackProvider initFeedbackProvider(XmlConfigSection config, MessageProvider messageProvider) throws ServicesException {
    try {
      String deployUrl = config.getString("deploy.url");
      return new FeedbackService(deployUrl, messageProvider);
    }
    catch (Exception e) {
      throw new ServicesException("Error during FeedbackProvider initialization.", e);
    }
  }

  public SenderProvider getSenderProvider() {
    return senderProvider;
  }

  public FeedbackProvider getFeedbackProvider() {
    return feedbackProvider;
  }

  public MessageProvider getMessageProvider() {
    return messageProvider;
  }
}
