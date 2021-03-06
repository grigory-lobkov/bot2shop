package com.bot2shop.storage;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Topic;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlParseDictionary<KeyWordType> implements IDictionary {

    List<Phrase<KeyWordType>> rawPhraseList; // temporary list for result
    private List<Topic> rawTopicList; // list of topics

    // logger
    private ILogger logger;

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    private void logError(Exception e) {
        logger.LogError(-1, null, e);
    }

    private void logError(String text) {
        logError(new RuntimeException(text));
    }

    private void logError(Exception e, String text) {
        Exception re = new RuntimeException(text + ": " + e.getMessage());
        re.setStackTrace(e.getStackTrace());
        logError(re);
    }

    // do actions, prepare connection/data
    public void process() {
        rawTopicList = new ArrayList<Topic>();
        rawPhraseList = new ArrayList<Phrase<KeyWordType>>();

        try {
            File fXmlFile = new File("resources/dictionary.xml/ITSchoolForKidsFAQ.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList phrasesList = doc.getChildNodes();
            processPhrases(phrasesList);

        } catch (Exception e) {
            logError(e);
        }
    }

    // returns list of phrases from storage
    public List<Phrase<KeyWordType>> getPhraseList() {
        return rawPhraseList;
    }

    // returns list of topics from storage
    public List<Topic> getTopicList() {
        return rawTopicList;
    }

    private void processPhrases(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            String name = node.getNodeName();
            if (name.compareTo("phrases") != 0) {
                logError("XmlParseDictionary. Root element must be \"phrases\", not \"" + name + "\".");
                continue;
            }

            NodeList topicList = node.getChildNodes();
            processTopic(topicList);
        }
    }

    private void processTopic(NodeList topicList) {
        for (int i = 0; i < topicList.getLength(); i++) {
            Node node = topicList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            String name = node.getNodeName();
            if (name.compareTo("topic") != 0) {
                logError("XmlParseDictionary. Element inside phrases must be \"topic\", not \"" + name + "\".");
                continue;
            }

            Topic t = new Topic();
            rawTopicList.add(t);
            if (node.hasAttributes()) {
                NamedNodeMap nodeMap = node.getAttributes();
                processTopicAtributes(t, nodeMap);
            }
            NodeList phraseList = node.getChildNodes();
            processPhrase(t, phraseList);
        }
    }

    private void processTopicAtributes(Topic t, NamedNodeMap nodeMap) {
        for (int k = 0; k < nodeMap.getLength(); k++) {
            Node node = nodeMap.item(k);
            String name = node.getNodeName();
            String value = node.getNodeValue();
            try {
                switch (name) {
                    case "name":
                        t.name = value;
                        break;
                    case "shortName":
                        t.shortName = value;
                        break;
                    default:
                        logError("XmlParseDictionary. Topic attribute \"" + name + "\"=\"" + value + "\" not implemented, yet.");
                        continue;
                }
            } catch (Exception e) {
                logError(e, "XmlParseDictionary. Topic attribute \"" + name + "\"=\"" + value + "\" cannot be parsed");
            }
        }
    }

    private void processPhrase(Topic topic, NodeList phraseList) {
        for (int i = 0; i < phraseList.getLength(); i++) {
            Node node = phraseList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            String name = node.getNodeName();
            if (name.compareTo("phrase") != 0) {
                logError("XmlParseDictionary. Element inside topic must be \"phrase\", not \"" + name + "\".");
                continue;
            }

            Phrase<KeyWordType> p = new Phrase<KeyWordType>();
            rawPhraseList.add(p);
            p.topicId = topic.id;
            if (node.hasAttributes()) {
                NamedNodeMap nodeMap = node.getAttributes();
                processPhraseAtributes(p, nodeMap);
            }
            if (node.hasChildNodes()) {
                NodeList paramList = node.getChildNodes();
                processPhraseElements(p, paramList);
            }
        }
    }

    private void processPhraseAtributes(Phrase<KeyWordType> p, NamedNodeMap nodeMap) {
        for (int k = 0; k < nodeMap.getLength(); k++) {
            Node node = nodeMap.item(k);
            String name = node.getNodeName();
            String value = node.getNodeValue();
            try {
                switch (name) {
//                    case "isTopicStart":
//                        p.isTopicStart = Boolean.parseBoolean(value);
//                        break;
                    case "isTopicUnknown":
                        p.isTopicUnknown = Boolean.parseBoolean(value);
                        break;
                    case "timeoutSec":
                        p.timeoutSec = Integer.parseInt(value);
                        break;
                    case "showChance":
                        p.showChance = Integer.parseInt(value);
                        break;
//                    case "unknownForTopics":
//                        String[] vals = value.split("[, ?.@]+");
//                        Topic[] topics = new Topic[vals.length];
//                        for (int i = vals.length - 1; i >= 0; i--) {
//                            topics[i] = Topic.valueOf(vals[i]);
//                        }
//                        p.unknownForTopics = topics;
//                        break;
                    case "action":
                        p.action = Phrase.Action.valueOf(value);
                        break;
                    default:
                        logError("XmlParseDictionary. Attribute \"" + name + "\"=\"" + value + "\" not implemented, yet.");
                        continue;
                }
            } catch (Exception e) {
                logError(e, "XmlParseDictionary. Attribute \"" + name + "\"=\"" + value + "\" cannot be parsed");
            }
        }
    }

    private void processPhraseElements(Phrase<KeyWordType> p, NodeList nodes) {
        for (int k = 0; k < nodes.getLength(); k++) {
            Node node = nodes.item(k);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            String name = node.getNodeName();
            String value = node.getTextContent();
            try {
                switch (name) {
                    case "id":
                        p.id = Integer.parseInt(value);
                        break;
                    case "title":
                        p.title = value;
                        break;
                    case "sayText":
                        p.sayText = value.replaceAll("\n[\\s]*", " ").replace("\\n", "\n");
                        break;
                    case "keyWords":
                        String[] vals = value.split("[, ?.@]+");
                        p.keyWords = vals;
                        break;
                    default:
                        logError("XmlParseDictionary. Element \"" + name + "\"=\"" + value + "\" not implemented, yet.");
                        continue;
                }
            } catch (Exception e) {
                logError(e, "XmlParseDictionary. Element \"" + name + "\"=\"" + value + "\" cannot be parsed");
            }
        }
    }

}