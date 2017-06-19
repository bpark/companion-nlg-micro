package com.github.bpark.companion;

import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.*;
import simplenlg.realiser.english.Realiser;

public class NlgDemo {

    public static void main(String[] args) {
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("chase");
        p.setObject("the monkey");
        p.addModifier("fast");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

        System.out.println(realiser.realiseSentence(p));

        DocumentElement sentence = nlgFactory.createSentence("hello Kurt");

        System.out.println(realiser.realiseSentence(sentence));

        SPhraseSpec q = nlgFactory.createClause("your day", "be");
        q.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        q.setFeature(Feature.TENSE, Tense.PAST);

        System.out.println(realiser.realiseSentence(q));

        AdvPhraseSpec adverbPhrase = nlgFactory.createAdverbPhrase("too slow");
        VPPhraseSpec verbPhrase = nlgFactory.createVerbPhrase("eating");
        verbPhrase.setFeature(Feature.TENSE, Tense.PAST);
        NPPhraseSpec nounPhrase = nlgFactory.createNounPhrase("Mary");
        nounPhrase.addModifier("you");

        SPhraseSpec clause = nlgFactory.createClause(nounPhrase, verbPhrase, adverbPhrase);
        System.out.println(realiser.realise(clause));

        VPPhraseSpec vb = nlgFactory.createVerbPhrase("be");
        vb.setFeature(Feature.TENSE, Tense.PAST);

        NLGElement you = nlgFactory.createNLGElement("you");
        you.setFeature(Feature.POSSESSIVE, true);
        NPPhraseSpec np = nlgFactory.createNounPhrase(you, "day");

        SPhraseSpec c = nlgFactory.createClause(np, vb);
        c.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);

        System.out.println(realiser.realise(c));
    }
}
