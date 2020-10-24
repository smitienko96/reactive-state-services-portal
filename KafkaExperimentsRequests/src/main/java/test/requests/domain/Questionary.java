package test.requests.domain;

import java.util.Map;

public class Questionary {

    private ApplicationType applicationType;

    private Map<DocumentFieldCode, String> questionsAndAnswers;


    public Questionary(ApplicationType applicationType, Map<DocumentFieldCode, String> questionsAndAnswers) {
        this.applicationType = applicationType;
        this.questionsAndAnswers = questionsAndAnswers;
    }

    public ApplicationType applicationType() { return applicationType; }

    public Map<DocumentFieldCode, String> questionsAndAnswers() {
        return questionsAndAnswers;
    }
}
