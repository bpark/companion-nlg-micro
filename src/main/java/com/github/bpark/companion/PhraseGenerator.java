package com.github.bpark.companion;

import com.github.bpark.companion.model.PhraseResponse;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * @author ksr
 */
public class PhraseGenerator {

    private NLGFactory nlgFactory;

    private Realiser realiser;


    public PhraseGenerator() {

        Lexicon lexicon = Lexicon.getDefaultLexicon();
        nlgFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);

    }

    public String generate(PhraseResponse phraseResponse) {

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject(phraseResponse.getSubject());
        p.setVerb(phraseResponse.getVerb());
        p.setObject(phraseResponse.getObject());

        return realiser.realiseSentence(p);
    }
}
